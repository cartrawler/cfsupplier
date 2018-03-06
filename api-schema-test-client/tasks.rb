
require 'singleton'
require_relative  './booking_life_cycle.rb'
require_relative  './schema_validation.rb'

module Tasks
  # Initialize
  class Setup
    include Singleton

    def initialize
      @action = Action.new
      @schema = Schema.new
    end

    def action
      @action.class
    end

    def schema
      @schema.class
    end
  end
  # Class
  class Action
    def self.search(url, post_data, auth_key)
      BookingLifeCycle::Setup.instance.target.search(url, post_data, auth_key)
    end

    def self.create(url, post_data, auth_key)
      BookingLifeCycle::Setup.instance.target.create(url, post_data, auth_key)
    end

    def self.cancel(url, post_data, auth_key)
      BookingLifeCycle::Setup.instance.target.cancel(url, post_data, auth_key)
    end

    def self.status(url, post_data, auth_key)
      BookingLifeCycle::Setup.instance.target.status(url, post_data, auth_key)
    end
  end
  # Class
  class Schema
    def self.validate(type, schema, data)
      SchemaValidation::Setup.instance.target.test(type, schema, data)
    end

    def self.fully_validate(type, schema, data)
      SchemaValidation::Setup.instance.target.test_fully(type, schema, data)
    end

  end
end
