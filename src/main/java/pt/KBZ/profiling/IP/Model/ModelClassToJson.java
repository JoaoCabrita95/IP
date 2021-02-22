package IP.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ModelClassToJson {
	
	public static JsonElement getProfileJson(Person person) {
		
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label", person.getLabel());
		jsonPropValue.addProperty("comment", person.getComment());
		jsonPropValue.addProperty("name", person.getName());
		jsonPropValue.addProperty("surname", person.getsurname());
		jsonPropValue.addProperty("gender", person.getGender());
		jsonPropValue.addProperty("nationality", person.getNationality());
		jsonPropValue.addProperty("address", person.getAddress());
		jsonPropValue.addProperty("driversLicense", person.getDriversLicense());
		jsonPropValue.addProperty("cvURI", person.getCVURI());
		jsonPropValue.addProperty("competenceArea", person.getCompetenceArea());
		jsonPropValue.addProperty("competenceAreaDescription", person.getCompetenceAreaDescription());
		jsonPropValue.addProperty("role", person.getRole());
		jsonPropValue.addProperty("currentJobURI", person.getCurrentJobURI());
		jsonPropValue.addProperty("uri", person.getURI());
		jsonPropValue.addProperty("id", RDFObject.uri2id(person.getURI()));
		
		return jsonPropValue;
		
	}
	
	public static JsonElement getCVJson(CV cv) {
		
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label", cv.getLabel());
		jsonPropValue.addProperty("comment", cv.getComment());
		jsonPropValue.addProperty("title", cv.getTitle());
		jsonPropValue.addProperty("personURI", cv.getPersonURI());
		jsonPropValue.addProperty("description", cv.getDescription());
		jsonPropValue.addProperty("targetSector", cv.getTargetSector());
		jsonPropValue.addProperty("otherInfo", cv.getOtherInfo());
		jsonPropValue.addProperty("currentJob", cv.getCurrentJob());
		jsonPropValue.addProperty("uri", cv.getURI());
		jsonPropValue.addProperty("id", RDFObject.uri2id(cv.getURI()));
		
		JsonArray jsonResults = new JsonArray();
		
		for(CVSkillRef skillRef : cv.getSkillRefs()) {
//        jsonResults.add(ModelClassToJson.getCVSkillRefJson(skillRef));
          if(skillRef.getSkillURI() != null)
              jsonResults.add(getCVSkillJsonFromRef(skillRef));
		}
		jsonPropValue.add("skills", jsonResults );
		
		jsonResults = new JsonArray();
		for(WorkHistory wh : cv.getWorkHistory()) {
			jsonResults.add(ModelClassToJson.getWorkHistoryJson(wh));
		}
		jsonPropValue.add("workHistory",jsonResults);
		
		for(Course course : cv.getCourses()) {
			jsonResults.add(ModelClassToJson.getCourseJson(course));
		}
		jsonPropValue.add("courses", jsonResults );
		
		jsonResults = new JsonArray();
		for(Education ed : cv.getEducation()) {
			jsonResults.add(ModelClassToJson.getEducationJson(ed));
		}
		jsonPropValue.add("education",jsonResults);
		
		return jsonPropValue;
	}
	
	public static JsonElement getJobJson(JobPosting job) {
		
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label", job.getLabel());
		jsonPropValue.addProperty("comment", job.getComment());
		jsonPropValue.addProperty("creator_id", job.getcreator_id());
		jsonPropValue.addProperty("jobDescription", job.getJobDescription());
		jsonPropValue.addProperty("contractType", job.getContractType());
		jsonPropValue.addProperty("sector", job.getSector());
		jsonPropValue.addProperty("occupation", job.getOccupation());
		jsonPropValue.addProperty("listingOrganization", job.getListingOrg());
		jsonPropValue.addProperty("hiringOrganization", job.getHiringOrg());
		jsonPropValue.addProperty("jobLocation", job.getjobLocation());
		jsonPropValue.addProperty("country", job.getCountry());
		jsonPropValue.addProperty("state", job.getState());
		jsonPropValue.addProperty("city", job.getCity());
		jsonPropValue.addProperty("startDate", job.getStartDate());
		jsonPropValue.addProperty("endDate", job.getEndDate());
		jsonPropValue.addProperty("seniorityLevel", job.getseniorityLevel());
		jsonPropValue.addProperty("expectedSalary", job.getExpectedSalary());
		jsonPropValue.addProperty("salaryCurrency", job.getSalaryCurrency());
		jsonPropValue.addProperty("uri", job.getURI());
		jsonPropValue.addProperty("id", RDFObject.uri2id(job.getURI()));
		
		JsonArray jsonResults = new JsonArray();
		
		for(SkillJobReq skillReq : job.getJobSkillReqRefs()) {
//          jsonResults.add(ModelClassToJson.getJobSkillRef(skillReq));
          jsonResults.add(getSkillReqJsonFromRef(skillReq));
		}
		jsonPropValue.add("skillReq", jsonResults );
		
		jsonResults = new JsonArray();
		for(WorkHistory wh : job.getworkExperienceReq()) {
			jsonResults.add(ModelClassToJson.getWorkHistoryJson(wh));
		}
		jsonPropValue.add("workExperienceReq",jsonResults);
		
		for(Course course : job.getCapabilityReq()) {
			jsonResults.add(ModelClassToJson.getCourseJson(course));
		}
		jsonPropValue.add("coursesReq", jsonResults );
		
		jsonResults = new JsonArray();
		for(Education ed : job.getEducationReq()) {
			jsonResults.add(ModelClassToJson.getEducationJson(ed));
		}
		jsonPropValue.add("educationReq",jsonResults);
		
		return jsonPropValue;
	}
	
	public static JsonElement getApplicationJson(Application application) {
			
		JsonObject jsonPropValue = new JsonObject();
			
		jsonPropValue.addProperty("label", application.getLabel());
		jsonPropValue.addProperty("comment", application.getComment());
		jsonPropValue.addProperty("personURI", application.getPersonURI());
		jsonPropValue.addProperty("jobURI", application.getJobURI());
		jsonPropValue.addProperty("expectedSalary", application.getExpectedSalary());
		jsonPropValue.addProperty("salaryCurrency", application.getSalaryCurrency());
		jsonPropValue.addProperty("availability", application.getAvailability());
		jsonPropValue.addProperty("uri", application.getURI());
		jsonPropValue.addProperty("id", RDFObject.uri2id(application.getURI()));
			
			
		return jsonPropValue;
	}
		
	public static JsonElement getSkillJson(Skill skill) {
		
        JsonArray temp = new JsonArray();
		
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label", skill.getLabel());
		jsonPropValue.addProperty("comment", skill.getComment());
//		jsonPropValue.addProperty("proficiencyLevel", skill.getProficiencyLevel());
//		jsonPropValue.addProperty("priorityLevel", skill.getPriorityLevel());
		jsonPropValue.addProperty("coreTo", skill.getCoreTo());
		jsonPropValue.addProperty("isFrom", skill.getIsFrom());
		jsonPropValue.addProperty("skillType", skill.getSkillType());
        jsonPropValue.addProperty("reuseLevel", skill.getReuseLevel());
        for(String synonim : skill.getSynonyms()) {
            temp.add(synonim);
        }
        jsonPropValue.add("synonyms", temp);
        temp = new JsonArray();
        
        for(String superClass : skill.getSuperClasses()) {
            temp.add(superClass);
        }
        jsonPropValue.add("superClasses", temp);
        temp = new JsonArray();
        
        for(String subClass : skill.getsubClasses()) {
            temp.add(subClass);
        }
        jsonPropValue.add("subClasses", temp);
        
		jsonPropValue.addProperty("uri", skill.getURI());
		jsonPropValue.addProperty("id", RDFObject.uri2id(skill.getURI()));
		
		
		return jsonPropValue;
	}
	
    
    public static JsonElement getCVSkillJsonFromRef(CVSkillRef skillRef) {
        
        Skill skill = Skill.getSkill(skillRef.getSkillURI());
        JsonObject result = (JsonObject) getSkillJson(skill);
        result.addProperty("proficiencyLevel", skillRef.getSkillLevel());
        result.addProperty("skillLastUsedDate", skillRef.getSkillLastUsedDate());
        result.addProperty("evalDate", skillRef.getEvalDate());
        result.addProperty("acquiredDate", skillRef.getAcquiredDate());
        result.addProperty("progress", skillRef.getProgress());
        
        return result;
    }
    
    public static JsonElement getSkillReqJsonFromRef(SkillJobReq skillRef) {
        
        Skill skill = Skill.getSkill(skillRef.getSkillURI());
        JsonObject jsonProps = (JsonObject) getSkillJson(skill);
        jsonProps.addProperty("proficiencyLevel", skillRef.getSkillLevel());
        jsonProps.addProperty("priorityLevel", skillRef.getPriorityLevel());
        
        return jsonProps;
    }
		
	public static JsonElement getWorkHistoryJson(WorkHistory workHistory) {
			
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label", workHistory.getLabel());
		jsonPropValue.addProperty("comment", workHistory.getComment());
		jsonPropValue.addProperty("position", workHistory.getPosition());
		jsonPropValue.addProperty("employer", workHistory.getEmployer());
		jsonPropValue.addProperty("from", workHistory.getFrom());
		jsonPropValue.addProperty("to", workHistory.getTo());
		jsonPropValue.addProperty("duration", workHistory.getDuration());
		jsonPropValue.addProperty("uri", workHistory.getURI());
		jsonPropValue.addProperty("id", RDFObject.uri2id(workHistory.getURI()));
		
		return jsonPropValue;
	}
		
	public static JsonElement getEducationJson(Education education) {
		
		
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label", education.getLabel());
		jsonPropValue.addProperty("comment", education.getComment());
		jsonPropValue.addProperty("major", education.getMajor());
		jsonPropValue.addProperty("minor", education.getMinor());
		jsonPropValue.addProperty("degreeType", education.getDegreeType());
		jsonPropValue.addProperty("from", education.getFrom());
		jsonPropValue.addProperty("to", education.getTo());
		jsonPropValue.addProperty("organization", education.getOrganization());
		jsonPropValue.addProperty("description", education.getDescription());
		jsonPropValue.addProperty("uri", education.getURI());
		jsonPropValue.addProperty("id", RDFObject.uri2id(education.getURI()));
		
			
		return jsonPropValue;
		}
	
	public static JsonElement getCourseJson(Course course) {
	
			
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label", course.getLabel());
		jsonPropValue.addProperty("comment", course.getComment());
		jsonPropValue.addProperty("qualification", course.getQualification());
		jsonPropValue.addProperty("organizedBy", course.getOrganizedBy());
		jsonPropValue.addProperty("isCertification", course.isCertification());
		jsonPropValue.addProperty("courseTitle", course.getCourseTitle());
		jsonPropValue.addProperty("courseDescription", course.getCourseDescription());
		jsonPropValue.addProperty("courseURL", course.getCourseURL());
		jsonPropValue.addProperty("startDate", course.getStartDate());
		jsonPropValue.addProperty("finishDate", course.getFinishDate());
		jsonPropValue.addProperty("uri", course.getURI());
		jsonPropValue.addProperty("id", RDFObject.uri2id(course.getURI()));
		
			
			
		return jsonPropValue;
	}

	//For rabbitMQ schema
	public static JsonElement getCVSkillRefJson(CVSkillRef ref) {
		
		Skill skill = Skill.getSkill(ref.getSkillURI());
		
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label", skill.getLabel());
		jsonPropValue.addProperty("comment", skill.getComment());
//		if(ref.getSkillURI() != null)
//			jsonPropValue.addProperty("skillID", ref.getSkillURI().substring(1));
//		jsonPropValue.addProperty("evalDate", ref.getEvalDate());
//		jsonPropValue.addProperty("acquiredDate", ref.getAcquiredDate());
//		jsonPropValue.addProperty("skillName", ref.getSkillName());
//		jsonPropValue.addProperty("skillLastUsedDate", ref.getSkillLastUsedDate());
		jsonPropValue.addProperty("proficiencyLevel", ref.getSkillLevel());
		jsonPropValue.addProperty("coreTo", skill.getCoreTo());
		jsonPropValue.addProperty("isFrom", skill.getIsFrom());
		jsonPropValue.addProperty("skillType", skill.getSkillType());
//		jsonPropValue.addProperty("progress", ref.getProgress());
		jsonPropValue.addProperty("uri", skill.getURI());
		jsonPropValue.addProperty("id", RDFObject.uri2id(skill.getURI()));
			
			
		return jsonPropValue;
	}
	
	public static JsonElement getJobSkillRef(SkillJobReq ref) {
		
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label", ref.getLabel());
		jsonPropValue.addProperty("comment", ref.getComment());
		if(ref.getSkillURI() != null)
			jsonPropValue.addProperty("skillID", ref.getSkillURI().substring(1));
		jsonPropValue.addProperty("skillName", ref.getSkillName());
		jsonPropValue.addProperty("proficiencyLevel", ref.getSkillLevel());
		jsonPropValue.addProperty("priorityLevel", ref.getPriorityLevel());
//		jsonPropValue.addProperty("uri", ref.getURI());
//		jsonPropValue.addProperty("id", RDFObject.uri2id(ref.getURI()));
			
			
		return jsonPropValue;
	}
}
