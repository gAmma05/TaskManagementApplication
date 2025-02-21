package model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

/**
 *
 * @author asus
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private Long project_id;
    private String project_name;
    private String description;
    private Date start_date;
    private Date end_date;
    private Status status;
    private Priority priority;
    private Double budget;
    private Integer manager_id;
    private Date created_at;
    private Date updated_at;

    public enum Status {
        NOT_STARTED("Not Started"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed");

        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static Status fromDisplayName(String displayName) {
            for (Status status : Status.values()) {
                if (status.getDisplayName().equalsIgnoreCase(displayName)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("No enum constant " + Status.class.getCanonicalName() + "." + displayName);
        }
    }

    public enum Priority {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High");

        private final String displayName;

        Priority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
