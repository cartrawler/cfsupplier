require 'net/https'
require 'uri'
require 'open-uri'
require 'json'
require 'date'

module SimpleHttp

  SSL_VERIFY_MODE = OpenSSL::SSL::VERIFY_NONE
  #SSL_VERIFY_MODE = OpenSSL::SSL::VERIFY_PEER

  # Class
  class HttpBase
    def ssl(http)
      if SimpleHttp::SSL_VERIFY_MODE == OpenSSL::SSL::VERIFY_PEER
        http.use_ssl = true
        http.ssl_version = :TLSv1
        http.verify_mode = SimpleHttp::SSL_VERIFY_MODE
      end
      http
    end
  end

  # Class
  class HttpGet < HttpBase
    def get(uri)
      http = Net::HTTP.new(uri.host, uri.port)
      ssl(http).request(Net::HTTP::Get.new(uri)).body
    end
  end
  # Class
  class HttpPost < HttpBase
    def post(uri, body, key)
      begin
        http = Net::HTTP.new(uri.host, uri.port)
        http.read_timeout = 60_000
        request = Net::HTTP::Post.new(uri.path, initheader = { 'Content-Type' => 'application/json', 'Authorization' => key })
        request.body = body
        response = ssl(http).request(request).body
        JSON.parse response
      rescue Timeout::Error, Errno::EINVAL, Errno::ECONNRESET, EOFError, Net::HTTPBadResponse, Net::HTTPHeaderSyntaxError, Errno::ECONNREFUSED, Net::ProtocolError, JSON::ParserError => e
        puts e
        puts " url where the server crashed = #{uri.host}#{uri.path} "
        data = { status: 'Nok', result: [] }
        data
      end
    end
  end
end
