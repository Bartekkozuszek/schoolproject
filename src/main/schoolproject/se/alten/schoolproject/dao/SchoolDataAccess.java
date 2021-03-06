package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.entity.Teacher;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.model.TeacherModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;
import se.alten.schoolproject.transaction.SubjectTransactionAccess;
import se.alten.schoolproject.transaction.TeacherTransactionAccess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Stream;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {


  private Student student = new Student();
  private StudentModel studentModel = new StudentModel();
  private Subject subject = new Subject();
  private SubjectModel subjectModel = new SubjectModel();
  private Teacher teacher = new Teacher();
  private TeacherModel teacherModel = new TeacherModel();

  @Inject
  StudentTransactionAccess studentTransactionAccess;

  @Inject
  SubjectTransactionAccess subjectTransactionAccess;

  @Inject
  TeacherTransactionAccess teacherTransactionAccess;


  @Override
  @SuppressWarnings("unchecked")
  public List listAllStudents() {
    System.out.println("listAllStudents() - SDA");
    List<StudentModel> sm = studentModel.toModelList(studentTransactionAccess.listAllStudents());
    return sm;
  }

  @Override
  public StudentModel addStudent(String newStudent) {
    Student studentToAdd = student.toEntity(newStudent);
    boolean checkForEmptyVariables = Stream.of(studentToAdd.getForename(), studentToAdd.getLastname(), studentToAdd.getEmail()).anyMatch(String::isBlank);
    if (checkForEmptyVariables) {
      studentToAdd.setForename("empty");
      return studentModel.toModel(studentToAdd);
    } else {
      studentTransactionAccess.addStudent(studentToAdd);
      List<Subject> subjects = subjectTransactionAccess.getSubjectByName(studentToAdd.getSubjects());
      subjects.forEach(sub -> {
        studentToAdd.getSubject().add(sub);
      });
      return studentModel.toModel(studentToAdd);
    }
  }

  @Override
  public StudentModel deleteStudent(String studentEmail) {
    Student removedstudent = studentTransactionAccess.deleteStudent(studentEmail);
    return studentModel.toModel(removedstudent);
  }

  @Override
  public StudentModel updateStudent(String forename, String lastname, String email) {
    Student studentToUpdate = new Student(forename, lastname, email);
    boolean emptyField = Stream.of(forename, lastname, email)
            .anyMatch(String::isBlank);
    if (emptyField) {
      studentToUpdate.setForename("empty");
      return studentModel.toModel(studentToUpdate);
    } else {
      Student temp = studentTransactionAccess.updateStudent(forename, lastname, email);
      return studentModel.toModel(studentToUpdate);
    }
  }

  @Override
  public StudentModel updateStudentPartial(String studentBody) {
    Student studentToUpdate = student.toEntity(studentBody);
    boolean emptyBody =
            Stream.of(studentToUpdate.getForename(),
                    studentToUpdate.getLastname(),
                    studentToUpdate.getEmail())
                    .allMatch(String::isBlank);
    if (emptyBody) {
      studentToUpdate.setForename("empty");
      return studentModel.toModel(studentToUpdate);
    } else {
      studentTransactionAccess.updateStudentPartial(studentToUpdate);
      return studentModel.toModel(studentToUpdate);
    }
  }

  @Override
  public List<StudentModel> findStudentsByName(String forename) {
    List<Student> foundedStudents = studentTransactionAccess.findStudentsByName(forename);
    List<StudentModel> studentModels = new ArrayList<>();
    for (Student s : foundedStudents) {
      studentModels.add(studentModel.toModel(s));
    }
    return studentModels;
  }

  public StudentModel findStudentByEmail(String email) {
    Student foundedStudent = studentTransactionAccess.findStudentByEmail(email);
    return studentModel.toModel(foundedStudent);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List listAllSubjects() {
    System.out.println("listAllSubjects() - SDA");
    List<SubjectModel> sm = subjectModel.toModelList(subjectTransactionAccess.listAllSubjects());
    return sm;
  }

  @Override
  public SubjectModel addSubject(String subjectModel) {
    if(subjectModel.isBlank()){
      return new SubjectModel("empty");
    }
    Subject subjectToAdd = subject.toEntity(subjectModel);
    subjectTransactionAccess.addSubject(subjectToAdd);
    return this.subjectModel.toModel(subjectToAdd);
  }

  @Override
  public String deleteSubject(String title) {
    if((title == null) || (title.isBlank())){
      return "empty";
    }
    return  subjectTransactionAccess.deleteSubject(title);
  }

  @Override
  public void addTeacherToSubject(Long id, String email) {

  }

  @Override
  public List<TeacherModel> listAllTeachers() {
    List<Teacher> test = teacherTransactionAccess.listAllTeachers();
    List<TeacherModel> teachersList = new ArrayList<>();
    test.forEach(t -> {
      teachersList.add(teacherModel.toModel(t));
    });
    return teachersList;
  }

  @Override
  public TeacherModel addTeacher(String newTeacher) {
    Teacher teacherToAdd = teacher.toEntity(newTeacher);
    boolean checkForEmptyVariables = Stream.of(teacherToAdd.getTeacherFirstName(), teacherToAdd.getTeacherLastName(), teacherToAdd.getTeacherEmail()).anyMatch(String::isBlank);

    if (checkForEmptyVariables) {
      teacherToAdd.setTeacherFirstName("empty");
      return teacherModel.toModel(teacherToAdd);
    } else {
      teacherTransactionAccess.addTeacher(teacherToAdd);

      List<Subject> subjects = subjectTransactionAccess.getSubjectByName(teacherToAdd.getSubjects());

      subjects.forEach(sub -> {
        teacherToAdd.getSubject().add(sub);
      });

      return teacherModel.toModel(teacherToAdd);
    }
  }

  @Override
  public void removeTeacher(String email) {
    teacherTransactionAccess.removeTeacher(email);
  }

  @Override
  public void updateTeacher(String firstName, String lastName, String email) {
    teacherTransactionAccess.updateTeacher(firstName, lastName, email);
  }


}

