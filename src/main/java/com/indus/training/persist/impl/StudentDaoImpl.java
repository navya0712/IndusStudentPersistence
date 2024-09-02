package com.indus.training.persist.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.indus.training.persist.dao.StudentDao;
import com.indus.training.persist.entity.Student;
import com.indus.training.persist.exceptions.InvalidStudentDataException;
import org.apache.logging.log4j.*;

/**
 * Implements the StudentDao interface and provides implementations CRUD methods
 * of Student entity
 */
public class StudentDaoImpl implements StudentDao {

	private static String studentDataDir;

	private static Logger loggerObj = LogManager.getLogger(StudentDaoImpl.class.getName());
	
	/**
	 * The Constructor that initializes the StudentDataDir path
	 */
	public StudentDaoImpl() {
		loadConfig();
	}

	/**
	 * Loads the Student Data Directory path from config properties and initializes
	 * it to the studentDataDir
	 */
	private void loadConfig() {
		Properties prop = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input != null) {
				prop.load(input);
				studentDataDir = prop.getProperty("student_data_path", "");
				loggerObj.info("StudentDaoImpl initialized with studentDataDir: " + studentDataDir);
			} else {
				loggerObj.log(Level.FATAL, "Config file not found.");
			}
		} catch (Exception ex) {
			loggerObj.log(Level.FATAL, "Failed to load config properties.", ex);
		}
	}

	/**
	 * Inserts a new Student into the data source.
	 * 
	 * @param stuObj The Student object to be inserted
	 * @return true if the insertion is successful; false otherwise
	 * @throws NullPointerException if the provided Student object is null
	 * @throws IOException          if an I/O error occurs during the insertion
	 */
	@Override
	public boolean insertStudent(Student stuObj) throws IOException {
		if (stuObj == null) {
			loggerObj.error("Student Object is null in insertStudent");
			throw new IllegalArgumentException("Student object cannot be null.");
		}

		File studentFile = new File(studentDataDir + "student" + stuObj.getStudentId() + ".txt");

		if (studentFile.exists()) {
			loggerObj.info("In insertStudent: A file with the provided Student ID already exists");
			return false;
		}

		try (FileWriter writer = new FileWriter(studentFile)) {
			writer.write(stuObj.getStudentId() + "," + stuObj.getFirstName() + "," + stuObj.getLastName());
			loggerObj.info("In insertStudent: Student inserted successfully with ID " + stuObj.getStudentId());
			return true;
		} catch (IOException e) {
			loggerObj.error("Failed to write student data to file", e);
			throw new IOException("Failed to write student data to file.", e);
		}
	}

	/**
	 * Fetches a Student from the data source based on the provided student ID.
	 * 
	 * @param studentId The ID of the student to be fetched
	 * @return The Student object associated with the provided student ID
	 * @throws IOException                 if an I/O error occurs during the
	 *                                     fetching
	 * @throws InvalidStudentDataException if the data associated with the student
	 *                                     ID is invalid
	 */
	@Override
	public Student fetchStudent(int studentId) throws IOException, InvalidStudentDataException {
		Student stuObj = null;
		File file = new File(studentDataDir + "student" + studentId + ".txt");

		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String[] studentInfo = reader.readLine().split(",");
				if (studentInfo.length >= 3) {
					stuObj = new Student(Integer.parseInt(studentInfo[0]), studentInfo[1], studentInfo[2]);
					loggerObj.info("In fetchStudent: Student data successfully fetched for ID " + studentId);
				} else {
					loggerObj.error("In fetchStudent: Incomplete student data in file for ID " + studentId);
					throw new InvalidStudentDataException("Incomplete student data in file.");
				}
			} catch (IOException e) {
				loggerObj.error("In fetchStudent: Error reading file for student ID " + studentId, e);
				throw e;
			}
		} else {
			loggerObj.warn("In fetchStudent: Student with Provided ID not found");
			throw new IOException("Student file not found.");
		}

		return stuObj;
	}

	/**
	 * Deletes a Student from the data source based on the provided student ID.
	 * 
	 * @param studentId The ID of the student to be deleted
	 * @return true if the deletion is successful; false otherwise
	 * @throws IOException if an I/O error occurs during the deletion
	 */
	@Override
	public boolean deleteStudent(int studentId) throws IOException {
		File file = new File(studentDataDir + "student" + studentId + ".txt");
		if (file.exists()) {
			if (file.delete()) {
				loggerObj.info("In deleteStudent: Student deleted Successfully for ID " + studentId);
				return true;
			} else {
				loggerObj.error("In deleteStudent: An error occured while deleting Student with ID " + studentId);
				throw new IOException("Unable to Delete Student Data");

			}
		}
		return false;
	}

	/**
	 * Updates the first name of a Student in the data source based on the provided
	 * student ID.
	 * 
	 * @param studentId    The ID of the student whose first name is to be updated
	 * @param updFirstName The new first name to be set for the student
	 * @return true if the update is successful; false otherwise
	 * @throws IOException if an I/O error occurs during the update
	 */
	@Override
	public boolean updateStudentFirstName(int studentId, String updFirstName) throws IOException {
		File file = new File(studentDataDir + "student" + studentId + ".txt");

		if (file.exists()) {
			String[] stuInfo;
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String line = reader.readLine();
				if (line != null) {
					stuInfo = line.split(",");
				} else {
					loggerObj.error("In updateStudentFirstName: Failed to read student data for ID " + studentId);
					throw new IOException("Failed to read student data.");
				}
			}
			Student updStuObj = new Student(Integer.parseInt(stuInfo[0]), updFirstName, stuInfo[2]);
			if (file.delete()) {
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
					writer.write(
							updStuObj.getStudentId() + "," + updStuObj.getFirstName() + "," + updStuObj.getLastName());
					loggerObj.info("In updateStudentFirstName: Student First Name Updated Successfully for Student ID "+studentId);
					return true;
				} catch (IOException e) {
					loggerObj.error("In updateStudentFirstName: An error occured while updating Student First Name for Student ID "+studentId); 
					throw new IOException("Failed to write updated student data.", e);
				}
			} else {
				loggerObj.error("In updateStudentFirstName: An error occured while deleting old Student File for Student ID "+studentId);
				throw new IOException("Failed to delete the old student file.");
			}
		} else {
			loggerObj.warn("In updateStudentFirstName: Student file not found for ID " + studentId);
			return false;
		}

	}

	/**
	 * Updates the last name of a Student in the data source based on the provided
	 * student ID.
	 * 
	 * @param studentId   The ID of the student whose last name is to be updated
	 * @param updLastName The new last name to be set for the student
	 * @return true if the update is successful; false otherwise
	 * @throws IOException if an I/O error occurs during the update
	 */
	@Override
	public boolean updateStudentLastName(int studentId, String updLastName) throws IOException {
		File file = new File(studentDataDir + "student" + studentId + ".txt");

		if (file.exists()) {
			String[] stuInfo;
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String line = reader.readLine();
				if (line != null) {
					stuInfo = line.split(",");
				} else {
					loggerObj.error("In updateStudentLastName: Failed to read student data for ID " + studentId);
					throw new IOException("Failed to read student data.");
				}
			}
			Student updStuObj = new Student(Integer.parseInt(stuInfo[0]), stuInfo[1], updLastName);
			if (file.delete()) {
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
					writer.write(
							updStuObj.getStudentId() + "," + updStuObj.getFirstName() + "," + updStuObj.getLastName());
					loggerObj.info("In updateStudentFirstName: Student Last Name Updated Successfully for Student ID "+studentId);
					return true;
				} catch (IOException e) {
					loggerObj.error("In updateStudentFirstName: An error occured while updating Student Last Name for Student ID "+studentId); 
					throw new IOException("Failed to write updated student data.", e);
				}
			} else {
				loggerObj.error("In updateStudentLastName: An error occured while deleting old Student File for Student ID "+studentId);
				throw new IOException("Failed to delete the old student file.");
			}
		} else {
			loggerObj.warn("In updateStudentLastName: Student file not found for ID " + studentId);
			return false;
		}
	}

}
