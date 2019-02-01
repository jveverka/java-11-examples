#!/bin/bash
# this test sends individual messages synchronously
# one message contains integer index and string message, for example: { index: 123, message: 'payload' }
# 1. sends 4x500 messages in warm-up period
# 2. sends synchronously 100k messages repeated 20x
# 3. evaluates results

./grpc-demo --host $1 --port 50051 getDataSync -w 500 -c 100000 -m payload -r 20
