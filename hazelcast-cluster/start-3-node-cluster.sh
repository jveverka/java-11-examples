#!/bin/bash

SESSION=$USER

tmux -2 new-session -d -s $SESSION
tmux new-window -t $SESSION:1 -n 'SplitTerminalExample'

tmux select-pane -t 0
tmux split-window -h -p 66
tmux select-pane -t 1
tmux split-window -h -p 50

tmux select-pane -t 0
tmux send-keys "j11; ./server-node/build/install/server-node/bin/server-node 3" C-m
tmux select-pane -t 1
tmux send-keys "j11; ./server-node/build/install/server-node/bin/server-node 3" C-m
tmux select-pane -t 2
tmux send-keys "j11; ./server-node/build/install/server-node/bin/server-node 3" C-m

tmux select-window -t $SESSION:1
tmux -2 attach-session -t $SESSION
