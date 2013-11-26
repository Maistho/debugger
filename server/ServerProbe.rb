require 'net/http'
require 'uri'

Net::HTTP.post_form URI('http://localhost:8008/probe'),
{ "q" => "ruby", "max" => "50"}

Net::HTTP.get(URI('http://localhost:8008/probe'))
