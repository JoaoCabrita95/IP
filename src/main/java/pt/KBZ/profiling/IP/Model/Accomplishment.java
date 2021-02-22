package IP.Model;

public class Accomplishment<E> {
	
	private String accomplishmentType;
	private E accomplishment;

	public Accomplishment(E accomplishment) {
		this.accomplishment = accomplishment;
		if(accomplishment instanceof Skill) {
			accomplishmentType = "Skill";
		}
		else if(accomplishment instanceof WorkHistory) {
			accomplishmentType = "WorkHistory";
		}
		else if(accomplishment instanceof Education) {
			accomplishmentType = "Education";
		}
		else if(accomplishment instanceof Course) {
			accomplishmentType = "Course";
		}
	}
	
	public String getAccomplishmentType() {
		return accomplishmentType;
	}
	
	public E getAccomplishment(){
		return accomplishment;
	}
}
