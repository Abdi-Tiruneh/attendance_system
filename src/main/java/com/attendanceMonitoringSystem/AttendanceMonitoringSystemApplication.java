package com.attendanceMonitoringSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AttendanceMonitoringSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceMonitoringSystemApplication.class, args);
	}

}
