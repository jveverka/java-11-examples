#!/bin/bash

SESSION=$USER

tmux -2 new-session -d -s $SESSION
tmux new-window -t $SESSION:1 -n 'AkkaClusterDemo'

#split screen into 3 horizontal parts
tmux select-pane -t 0
tmux split-window -v -p 66
tmux select-pane -t 1
tmux split-window -v -p 50

tmux select-pane -t 0
tmux send-keys "./docs/bin/start-node-01.sh" C-m
tmux select-pane -t 1
tmux send-keys "./docs/bin/start-node-02.sh" C-m
tmux select-pane -t 2
tmux send-keys "./docs/bin/start-node-03.sh" C-m

tmux select-window -t $SESSION:1
tmux -2 attach-session -t $SESSION
