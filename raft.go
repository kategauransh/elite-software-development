package raft

import (
	"sync"
	"time"
)

type NodeState int

const (
	Follower NodeState = iota
	Candidate
	Leader
)

func (s NodeState) String() string {
	return [...]string{"Follower", "Candidate", "Leader"}[s]
}

// LogEntry represents a state machine command and its associated Raft term
type LogEntry struct {
	Index int
	Term  int
	Cmd   interface{}
}

// RequestVoteArgs is the RPC payload for requesting votes from peers
type RequestVoteArgs struct {
	Term         int
	CandidateID  int
	LastLogIndex int
	LastLogTerm  int
}

// RequestVoteReply holds the voter response
type RequestVoteReply struct {
	Term        int
	VoteGranted bool
}

// AppendEntriesArgs represents the heartbeat / replication payload
type AppendEntriesArgs struct {
	Term         int
	LeaderID     int
	PrevLogIndex int
	PrevLogTerm  int
	Entries      []LogEntry
	LeaderCommit int
}

// AppendEntriesReply holds log replication updates
type AppendEntriesReply struct {
	Term    int
	Success bool
}
