package com.github.tommwq.applogmanagement.utility;

public class Util {
  
  /**
   * get current stack frame
   */
  public static StackTraceElement currentFrame() {
    StackTraceElement[] stack = new Throwable().getStackTrace();

    final int stackDepth = 2;
    if (stack.length < stackDepth) {
      throw new RuntimeException("cannot get stack information");
    }

    return stack[stackDepth - 1];
  }

  /**
   * get current time stamp
   */
  public static long currentTime() {
    return System.currentTimeMillis();
  }
}
