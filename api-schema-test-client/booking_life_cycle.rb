require 'singleton'
require_relative  './simplehttp.rb'
require 'json'

module BookingLifeCycle
  # Object Initialization
  class Setup
    include Singleton
    def initialize
      @target = Action.new
    end
    attr_reader :target
  end

  # Class definition
  class Action
    def communicate(url, post_data, auth_key, path)
      begin
        req = SimpleHttp::HttpPost.new
        reply = req.post(URI(url + path), post_data.to_json, auth_key)
      rescue StandardError => e
        puts " Something went wrong. #{e} "
        puts " Error at #{post_data.to_json}"
        reply = { status: 'Nok' }
      end
      reply
    end

    def search(url, post_data, auth_key)
      communicate(url, post_data, auth_key, '/v1/pricing/search')
    end

    def create(url, post_data, auth_key)
      communicate(url, post_data, auth_key, '/v1/booking/create')
    end

    def cancel(url, post_data, auth_key)
      communicate(url, post_data, auth_key, '/v1/booking/cancel')
    end

    def status(url, post_data, auth_key)
      communicate(url, post_data, auth_key, '/v1/booking/status')
    end
  end
end
