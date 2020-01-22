package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@Stateless
@NoArgsConstructor
@Path("/fill")
public class TestData {

  @Inject
  private SchoolAccessLocal sal;

  @GET
  @Produces({"application/JSON"})
  public Response fill() {
    populateSubjects();
    populateStudents();
    return Response.ok().entity("{\"Test data for adding DB!\"}").build();
  }

  private void populateStudents(){
    List<String> students = new ArrayList<>();
    students.add("{\n" +
            "    \"forename\": \"Frank\",\n" +
            "    \"lastname\": \"Lampard\",\n" +
            "    \"email\": \"frank@com\",\n" +
            "    \"subjects\": [\n" +
            "        \"JS\",\n" +
            "        \"JS\"\n" +
            "    ]\n" +
            "}");
    students.add("{\n" +
            "    \"forename\": \"Ashley\",\n" +
            "    \"lastname\": \"Cole\",\n" +
            "    \"email\": \"ashley@hotmail.com\",\n" +
            "    \"subjects\": [\n" +
            "        \"Java\",\n" +
            "        \"Java\"\n" +
            "    ]\n" +
            "}");

    students.stream().forEach(s -> sal.addStudent(s));
  }

  private void populateSubjects(){
    List<String> subjects = new ArrayList<>();
    subjects.add("{\"subject\":\"JS\"}");
    subjects.add("{\"subject\":\"Java\"}");
    subjects.stream().forEach(s -> sal.addSubject(s));
  }
}
