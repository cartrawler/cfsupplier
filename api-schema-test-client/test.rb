require 'singleton'
require 'json'
require 'securerandom'
require 'thread'
require_relative './tasks.rb'

module SupplierApi
     
    CONFIG = JSON.parse(File.read('.config/config.json'))
    BOOKING_GENERIC_SEARCH_REQUEST = JSON.parse(File.read('./schemaValidation/genericSearchRequestSchema.json')) 
    BOOKING_GENERIC_SEARCH_RESPONSE = JSON.parse(File.read('./schemaValidation/genericSearchResponseSchema.json')) 
    BOOKING_STATUS_RESPONS = JSON.parse(File.read('./schemaValidation/bookingStatusResponseSchema.json')) 
    BOOKING_CANCEL_OR_STATUS_REQUEST = JSON.parse(File.read('./schemaValidation/bookingCancelorStatusRequestSchema.json')) 
    BOOKING_CREATE_REQUEST = JSON.parse(File.read('./schemaValidation/bookingCreateRequestSchema.json')) 
    BOOKING_CREATE_RESPONSE = JSON.parse(File.read('./schemaValidation/bookingCreateResponseSchema.json')) 
    BOOKING_CANCEL_RESPONSE = JSON.parse(File.read('./schemaValidation/bookingCancelResponseSchema.json')) 
    ADDRESS_JSON = JSON.parse(File.read( './config/address.json' ))

    URL = CONFIG["baseUrl"]
    AUTHKEY = CONFIG["authKey"]


    class Setup
        include Singleton
        def initialize
            @target = Test.new
        end

        def target
            @target 
        end
    end
    
    class Test

        def cleanup
            @threads = nil
        end


        def check_config
         
            if SupplierApi::URL.empty? or SupplierApi::AUTHKEY.empty? 
                abort("Please, provide valid URL and Authentication key")
            end
        end

        def status_and_result_check(data)
            success = false
            if data["status"].to_s.downcase.eql?('ok') and data['result'].length > 0

                success = true
            end
            success
        end

        def status_check(data)
            success = false
            if data["status"].to_s.downcase.eql?('ok') 
                success = true
            end
            success
        end

        def generate_random_number(size)
            SecureRandom.random_number(size) 
        end

        def generate_random_hex
            SecureRandom.hex 
        end

        def search
            check_config
            hash_data = SupplierApi::CONFIG
            url = SupplierApi::URL
            post_data = hash_data["searchDataGenericProduct"] 
            post_data[:pickupDateTime] = (Time.now + 1296000).strftime("%Y-%m-%dT%H:%M") 
            valid = Tasks::Setup.instance.schema.validate("Search Request ", BOOKING_GENERIC_SEARCH_REQUEST, post_data)
            if valid 
                auth_key = SupplierApi::AUTHKEY
                reply = Tasks::Setup.instance.action.search(url, post_data, auth_key)
                
                if status_and_result_check(reply) 
                    puts "-received #{reply["result"].length } offers "
                    valid = Tasks::Setup.instance.schema.validate("Search Response ", BOOKING_GENERIC_SEARCH_RESPONSE, reply)
                    if valid then "-search completed" else puts "-though the search is completed, your response json is invalid!" end 
                else
                    puts "- no offers"
                end
            else
                puts "-Sorry your input json is invalid. Can not proceed further"
            end
            if valid
                reply
            else
                reply = {status:'Nok', result:[]}
            end
            reply   
        end

        def create
            check_config
            searchReply = search 
            valid = status_and_result_check(searchReply)
            if valid
                puts "-now selecting one of the offer and creating a booking"
                booking_id = generate_random_hex 
                shortId =  generate_random_number(999999999)  
                firstOffer = searchReply["result"][0]
                product = firstOffer["product"] 
                priceTotal = firstOffer["price"]
                supplierPricingRef = firstOffer["supplierPricingRefId"] 
                hash_data = SupplierApi::CONFIG
                url = SupplierApi::URL
                auth_key =  SupplierApi::AUTHKEY
                post_dataTemp = hash_data["bookingCreateData"] 
                post_data = post_dataTemp.clone
                post_data["pickupDateTime"] = (Time.now + 1296000).strftime("%Y-%m-%dT%H:%M")
                post_data["bookingRefId"] = booking_id
                post_data["shortId"] = shortId.to_s
                post_data["supplierPricingRefId"] = supplierPricingRef
                post_data["product"] = product
                post_data["price"] = priceTotal
                puts "-data used for creating booking \n ============================================================== \n #{post_data} \n ==============================================================" 
                valid = Tasks::Setup.instance.schema.validate("Create Request ", BOOKING_CREATE_REQUEST, post_data)
                if valid
                    puts "-now creating the booking"
                    reply = Tasks::Setup.instance.action.create(url, post_data, auth_key)
                    if status_check(reply)
                        puts "-supplierBookingRefId =#{reply["supplierBookingRefId"]}"
                        valid = Tasks::Setup.instance.schema.validate("Create Response ", BOOKING_CREATE_RESPONSE, reply)
                        if valid then "-create completed" else puts "-though the create is completed, your response json is invalid!" end 
                    end
                else
                    puts "-Sorry your input json is invalid. Can not proceed further"
                end
            end
            booking_id
        end

        def cancel(id)
            check_config
            post_data = {
                bookingRefId:id
            }
            valid = Tasks::Setup.instance.schema.validate("Cancel Request ", BOOKING_CANCEL_OR_STATUS_REQUEST, post_data)
            if valid 
                url = SupplierApi::URL
                auth_key =  SupplierApi::AUTHKEY
                reply = Tasks::Setup.instance.action.cancel(url, post_data, auth_key)
                if status_check(reply)
                    valid = Tasks::Setup.instance.schema.validate("Cancel Response ", BOOKING_CANCEL_RESPONSE, reply)
                    if valid then "-cancel completed" else puts "-though the cancel is completed, your response json is invalid!" end 
                else
                    puts "-cancel operation failed. #{reply.to_json}"
                end 
            else
                puts "-Sorry your input json is invalid. Can not proceed further"
            end
        end

        def status(id)
            check_config
            post_data = {
                bookingRefId:id
            }
            puts "-checking status of = #{id}"
            valid = Tasks::Setup.instance.schema.validate("Status Request ", BOOKING_CANCEL_OR_STATUS_REQUEST, post_data)
            if valid 
                url = SupplierApi::URL
                auth_key =  SupplierApi::AUTHKEY
                reply = Tasks::Setup.instance.action.status(url, post_data, auth_key)
                if status_check(reply)
                    valid = Tasks::Setup.instance.schema.validate("Status Response ", BOOKING_STATUS_RESPONS, reply)
                    if valid then "-status completed" else puts "-though the cancel is completed, your response json is invalid!" end 
                else
                    puts "-status operation failed. #{reply.to_json}"
                end 

            else
                puts "-Sorry your input json is invalid. Can not proceed further"
            end
        end

        def complete
            check_config
            booking_id = create
            status(booking_id)
            cancel(booking_id)
            status(booking_id)
        end

        def searchWithData(post_data)
            url = SupplierApi::URL
            auth_key = SupplierApi::AUTHKEY
            startTime = Time.now
            reply = Tasks::Setup.instance.action.search(url, post_data, auth_key)
            if status_and_result_check(reply) 
                endTime = Time.now
                diff = endTime - startTime
                puts "-received #{reply["result"].length } offers in #{diff} "
            end
        end

        def performance(size)
            check_config
            Thread.abort_on_exception = true 
            SupplierApi::ADDRESS_JSON.each do |k,v|   
                t = Time.now + 1296000  
                tempData = v
                tempData["pickupDateTime"] = t.strftime("%Y-%m-%dT%H:%M") 
                puts "#{tempData["addresses"][0]["address"]} - #{tempData["addresses"][1]["address"]}"  
                #puts "job started at = #{Time.now().strftime("%Y-%m-%dT%H:%M:%S")}" 
                @queue = (1..size).inject(Queue.new, :push)
                @threads = Array.new(size) do
                  Thread.new do
                    until @queue.empty? 
                        searchWithData(tempData)
                        next_object = @queue.shift 
                        sleep 1
                    end
                  end
                end
                begin 
                  @threads.each(&:join)
                ensure
                  cleanup
                end   
            end 
        end
    end
end


var1 = ARGV[0].to_i
var2 = ARGV[1]


case var1
when 1
    SupplierApi::Setup.instance.target.search
when 2
    SupplierApi::Setup.instance.target.create
when 3
    SupplierApi::Setup.instance.target.cancel(var2)
when 4
    SupplierApi::Setup.instance.target.status(var2)
when 5
    SupplierApi::Setup.instance.target.complete
when 6
    SupplierApi::Setup.instance.target.performance(var2.to_i)
end


