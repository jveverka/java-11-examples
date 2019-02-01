#!/bin/bash
# this test sends individual messages asynchronously
# one message contains integer index and string message, for example: { index: 123, message: 'payload' }
# 1. sends 4x500 messages in warm-up period
# 2. sends asynchronously 100k messages repeated 20x
# 3. evaluates results

./grpc-demo --host $1 --port 50051 getDataAsync -w 500 -c 100000 -m payload -r 20
