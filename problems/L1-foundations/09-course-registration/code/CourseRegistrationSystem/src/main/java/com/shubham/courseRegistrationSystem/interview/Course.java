package com.shubham.courseRegistrationSystem.interview;

import java.util.List;

public record Course(String id, int credits, List<String> prereqIds) {}
