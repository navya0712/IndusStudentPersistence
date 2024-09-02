package com.indus.training.persist.dao;


import com.indus.training.persist.entity.Student;
import com.indus.training.persist.impl.StudentDaoImpl;

import junit.framework.TestCase;

public class TestStudentDaoImpl extends TestCase {

	private StudentDaoImpl stuDaoImpl = null;

	protected void setUp() throws Exception {
		stuDaoImpl = new StudentDaoImpl();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testInsertStudent() {
		Student stuObj = new Student(1026, "Navya", "Bade");
		try {
			System.out.println(stuDaoImpl.insertStudent(stuObj));
		} catch (Exception e) {
			System.out.print("IO Exception");
		}

	}

	public void testFetchStudent() {
		int stuId = 1026;
		try {
			Student stuObj = stuDaoImpl.fetchStudent(stuId);
			System.out.println(stuObj.toString());
		} catch (Exception e) {
			System.out.print(e);
		}

	}

//	public void testDeleteStudent() {
//		int stuId = 1026;
//		try {
//			System.out.print(stuDaoImpl.deleteStudent(stuId));
//		} catch (Exception e) {
//			System.out.print(e);
//		}
//
//	}

	public void testUpdateStudentFirstName() {
		int stuId = 1026;
		try {
			System.out.println(stuDaoImpl.updateStudentFirstName(stuId, "Teja"));
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void testUpdateStudentLastName() {
		int stuId = 1026;
		try {
			System.out.println(stuDaoImpl.updateStudentLastName(stuId, "Badeeeee"));
		} catch (Exception e) {
			System.out.print(e);
		}
	}

}
