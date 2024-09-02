package com.indus.training.persist.dao;

import java.io.IOException;

import com.indus.training.persist.entity.Student;
import com.indus.training.persist.exceptions.InvalidStudentDataException;

/**
 * Interface for performing CRUD operations on
 * Student entities.
 */
public interface StudentDao {

	/**
	 * Inserts a new Student into the data source.
	 * 
	 * @param stuObj The Student object to be inserted
	 * @return true if the insertion is successful; false otherwise
	 * @throws NullPointerException if the provided Student object is null
	 * @throws IOException          if an I/O error occurs during the insertion
	 */
	public boolean insertStudent(Student stuObj) throws NullPointerException, IOException;

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
	public Student fetchStudent(int studentId) throws IOException, InvalidStudentDataException;

	/**
	 * Deletes a Student from the data source based on the provided student ID.
	 * 
	 * @param studentId The ID of the student to be deleted
	 * @return true if the deletion is successful; false otherwise
	 * @throws IOException if an I/O error occurs during the deletion
	 */
	public boolean deleteStudent(int studentId) throws IOException;

	/**
	 * Updates the first name of a Student in the data source based on the provided
	 * student ID.
	 * 
	 * @param studentId    The ID of the student whose first name is to be updated
	 * @param updFirstName The new first name to be set for the student
	 * @return true if the update is successful; false otherwise
	 * @throws IOException if an I/O error occurs during the update
	 */
	public boolean updateStudentFirstName(int studentId, String updFirstName) throws IOException;

	/**
	 * Updates the last name of a Student in the data source based on the provided
	 * student ID.
	 * 
	 * @param studentId   The ID of the student whose last name is to be updated
	 * @param updLastName The new last name to be set for the student
	 * @return true if the update is successful; false otherwise
	 * @throws IOException if an I/O error occurs during the update
	 */
	public boolean updateStudentLastName(int studentId, String updLastName) throws IOException;

}
