#!/bin/bash

#python -m grpc_tools.protoc -I../src --python_out=grpc --grpc_python_out=grpc ../src/main/proto/service.proto
python3 -m grpc_tools.protoc -I../src --python_out=grpc --grpc_python_out=grpc ../src/main/proto/service.proto

