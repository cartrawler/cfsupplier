require 'json-schema'
require 'singleton'
module SchemaValidation
  # Initialization
  class Setup
    include Singleton
    attr_reader :target
    def initialize
      @target = Test.new
    end
  end
  # Class
  class Test
    def validate(schema, data)
      begin
        JSON::Validator.validate!(schema, data)
      rescue JSON::Schema::ValidationError => e
        puts e.message
        false
      rescue StandardError => e
        puts e.message
        false
      end
    end

    def fully_validate(schema, data)
      begin
        JSON::Validator.fully_validate(schema, data)
      rescue JSON::Schema::ValidationError => e
        puts e.message
        false
      rescue StandardError => e
        puts e.message
        false
      end
    end

    def test(type, schema, data)
      success = validate(schema, data)
      puts "-#{type} Schema Validation = #{success}"
      success
    end

    def test_fully(type, schema, data)
      success = fully_validate(schema, data)
      puts "-#{type} Schema Validation = #{success}"
      success
    end
  end
end
