#!/usr/bin/env ruby
require "net/http"
require "socket"
require "uri"
require 'json'

SOCKADDR = Socket.pack_sockaddr_in(8008,"10.37.0.197")
E = "\r\n\r\n"

def test request
	socket = Socket.new(Socket::AF_INET, Socket::SOCK_STREAM, 0)
	socket.connect(SOCKADDR)
	socket.write request
	results = socket.read
	socket.close
	puts results
end

test({"method" => "getBug",	"id" => "p0001"}.to_json + E)
test({"method" => "getBug",	"difficulty" => "EASY", "language" => "PYTHON2"}.to_json + E)
test({"method" => "postBug",	"code" => "print(123)", "id" => "p0000"}.to_json + E)
test({"method" => "postBug",	"code" => "def fib(n):\n\tif n <= 1:\n\t\treturn n\n\telse:\n\t\treturn fib(n)+fib(n-1)\n\n", "id" => "p0000"}.to_json + E)
test({"method" => "postBug",	"code" => "def fib(n):\n\tif n <= 1:\n\t\treturn n\n\telse:\n\t\treturn fib(n-1)+fib(n-2)\n\n", "id" => "p0000"}.to_json + E)
test({"method" => "postBug",	"code" => "def fib(n):\n\tif n <= 1:\n\t\treturn n\n\telse:\n\t\treturn fib(n-1)+fib(n-3)\n\n", "id" => "p0000"}.to_json + E)
