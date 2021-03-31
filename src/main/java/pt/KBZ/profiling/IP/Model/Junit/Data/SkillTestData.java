package IP.Model.Junit.Data;

import java.util.ArrayList;
import java.util.List;

import IP.Model.Skill;


public class SkillTestData {

	List<Skill> Ss = new ArrayList<Skill>();
	int counter = 0;
	
	public SkillTestData() {
		
		Ss.add(new Skill("Skill_1", "Java", "advanced", "high", "Skill comment 1", "cv:Skill"));
		Ss.add(new Skill("Skill_2", "Python", "expert", "high", "Skill comment 2", "cv:Skill"));
		Ss.add(new Skill("Skill_3", "C++", "basic", "high", "Skill comment 3", "cv:Skill"));
		Ss.add(new Skill("Skill_4", "Programing", "basic", "high", "Skill comment 4", "cv:Skill"));
		Ss.add(new Skill("Skill_5", "Nodejs", "basic", "high", "Skill comment 5", "cv:Skill"));
		Ss.add(new Skill("Skill_6", "Angular", "basic", "high", "Skill comment 6", "cv:Skill"));
		Ss.add(new Skill("Skill_7", "Javascript", "basic", "high", "Skill comment 7", "cv:Skill"));
		Ss.add(new Skill("Skill_8", "Blockchain", "basic", "high", "Skill comment 8", "cv:Skill"));
		Ss.add(new Skill("Skill_9", "PHP", "basic", "high", "Skill comment 9", "cv:Skill"));
		Ss.add(new Skill("Skill_10", "C#", "basic", "high", "Skill comment 10", "cv:Skill"));
		Ss.add(new Skill("Skill_11", "R", "basic", "high", "Skill comment 11", "cv:Skill"));
		Ss.add(new Skill("Skill_12", "Tensorflow", "expert", "high", "Skill comment 12", "cv:Skill"));
		Ss.add(new Skill("Skill_13", "Angularjs", "basic", "high", "Skill comment 13", "cv:Skill"));
		Ss.add(new Skill("Skill_14", "Machine Learning", "basic", "high", "Skill comment 14", "cv:Skill"));
		Ss.add(new Skill("Skill_15", "Neural Network", "advanced", "high", "Skill comment 15", "cv:Skill"));
		Ss.add(new Skill("Skill_16", "MySQL", "basic", "high", "Skill comment 16", "cv:Skill"));
		Ss.add(new Skill("Skill_17", "Postgresql", "basic", "high", "Skill comment 17", "cv:Skill"));
		Ss.add(new Skill("Skill_18", "MongoDB", "basic", "high", "Skill comment 18", "cv:Skill"));
		Ss.add(new Skill("Skill_19", "SQL", "basic", "high", "Skill comment 19", "cv:Skill"));
		Ss.add(new Skill("Skill_20", "Apache Spark", "advanced", "high", "Skill comment 20", "cv:Skill"));
		
		Ss.get(0).addsubClass(Ss.get(4).getURI());
		Ss.get(0).addsubClass(Ss.get(6).getURI());
		Ss.get(0).addsubClass(Ss.get(12).getURI());
		
		Ss.get(3).addsubClass(Ss.get(0).getURI());
		Ss.get(3).addsubClass(Ss.get(1).getURI());
		Ss.get(3).addsubClass(Ss.get(2).getURI());
		Ss.get(3).addsubClass(Ss.get(4).getURI());
		Ss.get(3).addsubClass(Ss.get(5).getURI());
		Ss.get(3).addsubClass(Ss.get(6).getURI());
		Ss.get(3).addsubClass(Ss.get(8).getURI());
		Ss.get(3).addsubClass(Ss.get(9).getURI());
		Ss.get(3).addsubClass(Ss.get(12).getURI());
		Ss.get(3).addsubClass(Ss.get(15).getURI());
		
		Ss.get(0).addSuperClass(Ss.get(3).getURI());
		Ss.get(1).addSuperClass(Ss.get(3).getURI());
		Ss.get(2).addSuperClass(Ss.get(3).getURI());
		Ss.get(4).addSuperClass(Ss.get(3).getURI());
		Ss.get(5).addSuperClass(Ss.get(3).getURI());
		Ss.get(6).addSuperClass(Ss.get(3).getURI());
		Ss.get(8).addSuperClass(Ss.get(3).getURI());
		Ss.get(9).addSuperClass(Ss.get(3).getURI());
		Ss.get(12).addSuperClass(Ss.get(3).getURI());
		Ss.get(15).addSuperClass(Ss.get(3).getURI());
		
		Ss.get(4).addSuperClass(Ss.get(0).getURI());
		Ss.get(6).addsubClass(Ss.get(12).getURI());
		Ss.get(12).addSuperClass(Ss.get(6).getURI());
		
		Ss.get(6).addSuperClass(Ss.get(0).getURI());
		Ss.get(4).addSuperClass(Ss.get(6).getURI());
		Ss.get(6).addsubClass(Ss.get(4).getURI());
	}
	
	public List<Skill> getSkills(){
		return Ss;
	}
	
	public Skill getSkillByURI(String URI) {
		for(Skill s: Ss) {
			if(s.getURI().equals(URI))
				return s;
		}
		return null;
	}
	
	public Skill getSkillByLabel(String label) {
		for(Skill s: Ss) {
			if(s.getLabel().equals(label))
				return s;
		}
		return null;
	}
	
	//Get different Lists of skills for the different CVs depending on the CV id, for insertion and testing purposes
	//Temporarily returns three skills through a function that rotates between the available skills
	public List<Skill> getSkillsForCVs(String id){
		List<Skill> skills = new ArrayList<Skill>();
		for(int i = 0; i < 3; i++) {
			if(counter < Ss.size()) {
				skills.add(Ss.get(counter++));
			}	
			else {
				counter = 0;
				skills.add(Ss.get(counter++));
			}
		}
		return skills;
	}
	
	//Get different Lists of skills for the different JobPostings depending on the JobPosting id, for insertion and testing purposes
	//Temporarily returns three skills through a function that rotates between the available skills
	public List<Skill> getSkillsForJobPostings(String id){
		List<Skill> skills = new ArrayList<Skill>();
		for(int i = 0; i < 3; i++) {
			if(counter < Ss.size()) {
				skills.add(Ss.get(counter++));
				counter++;
			}	
			else {
				counter = counter % Ss.size();
				skills.add(Ss.get(counter++));
				counter *= 2;
				counter++;
			}
		}
		return skills;
	}
}
