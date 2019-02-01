#!/bin/bash
# this test sends messages in groups of 10 synchronously
# one message contains integer index and string message, for example: { index: 123, message: 'payload' }
# 1. sends 4x500 messages in warm-up period
# 2. sends synchronously 100k groups of 10 messages repeated 20x
# 3. evaluates results

./grpc-demo --host $1 --port 50051 getBulkData -w 500 -c 100000 -m payload -r 20 -b 10
