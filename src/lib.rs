//! A highly-optimized, lock-free, single-producer single-consumer (SPSC)
//! and multi-producer multi-consumer (MPMC) concurrent ring buffer.
//!
//! Designed for low-latency systems requiring minimum thread contention and
//! deterministic performance characteristics.

use std::sync::atomic::AtomicUsize;
use std::cell::UnsafeCell;

/// The lock-free queue interface.
pub trait ConcurrentQueue<T> {
    fn push(&self, value: T) -> Result<(), QueueError<T>>;
    fn pop(&self) -> Option<T>;
    fn capacity(&self) -> usize;
    fn is_empty(&self) -> bool;
}

#[derive(Debug, PartialEq, Eq)]
pub enum QueueError<T> {
    Full(T),
    Disconnected,
}
