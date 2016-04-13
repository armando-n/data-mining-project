package domain.id3;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/*
 * The only purpose of this class is to group an attribute
 * title with all of its possible values.
 */
public class Attribute {
    private String attributeTitle;
    private Set<String> attributeValues;

    public Attribute(String title, Collection<String> values) {
        this.attributeTitle = title;
        this.attributeValues = new HashSet<String>(values);
    }
    
    public String getTitle() {
        return attributeTitle;
    }
    
    public Set<String> getValues() {
        return attributeValues;
    }

}
