require 'singleton'

module DependencyChecker
  # Object Initialize
  class Gemchecker
    include Singleton
    attr_reader :checker
    def initialize
      @checker = Gems.new
    end
  end
  
  # class
  class Gems
    def list(list_of_required_gems, list_of_installed_gem)
      found = false
      not_installed = []
      list_of_required_gems.each do |x|
        found = list_of_installed_gem.include?(x.strip)
        if !found
          not_installed << x.strip
        else
          puts "#{x.strip} is available."
        end
      end
      not_installed
    end

    def check_gem_dependency
      begin
        read_file = Thread.new {
          list_of_required_gems = []
          File.open('./config/gemRequirement.txt', 'r') { |file|
            file.each do |line|
              list_of_required_gems << line.strip
            end
          }
          list_of_installed_gem = []
          Gem::Specification.find_all.map { |spec|
            list_of_installed_gem << spec.name.strip
          }
          not_installed = list(list_of_required_gems, list_of_installed_gem) 
          if !not_installed.empty?
            not_installed.each do |x|
              puts "NOW INSTALLING - #{x}"
              system "sudo gem install #{x}"
            end
          else
            puts 'All required gems are available in the system'
          end
        }
        read_file.join
      rescue StandardError => e
        puts " Reading Dependency file error = #{e}"
      end
    end
  end
end

DependencyChecker::Gemchecker.instance.checker.check_gem_dependency
