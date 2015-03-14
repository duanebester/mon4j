package link.duane.mon4j.alerts;

import java.util.Date;
import java.util.Objects;

/**
 * @author duanebester
 */
public class Alert
{
    private String category;

    private String info;

    private int priority;

    private String key;

    private String summary;

    private Date created;

    public Alert()
    {
    }

    public Alert( String category, String info, int priority, String key, String summary )
    {
        this.category = category;
        this.info = info;
        this.priority = priority;
        this.key = key;
        this.summary = summary;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode( this.category );
        hash = 31 * hash + Objects.hashCode( this.info );
        hash = 31 * hash + this.priority;
        hash = 31 * hash + Objects.hashCode( this.key );
        hash = 31 * hash + Objects.hashCode( this.summary );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final Alert other = (Alert) obj;
        if ( !Objects.equals( this.category, other.category ) )
        {
            return false;
        }
        if ( !Objects.equals( this.info, other.info ) )
        {
            return false;
        }
        if ( this.priority != other.priority )
        {
            return false;
        }
        if ( !Objects.equals( this.key, other.key ) )
        {
            return false;
        }
        return Objects.equals( this.summary, other.summary );
    }

    @Override
    public String toString()
    {
        return "Alert{" + "category=" + category + ", info=" + info + ", priority=" + priority + ", key=" + key + ", summary=" + summary + '}';
    }

    /**
     * @return the category
     */
    public String getCategory()
    {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory( String category )
    {
        this.category = category;
    }

    /**
     * @return the info
     */
    public String getInfo()
    {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo( String info )
    {
        this.info = info;
    }

    /**
     * @return the priority
     */
    public int getPriority()
    {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority( int priority )
    {
        if ( priority >= 10 )
        {
            priority = 10;
        }

        this.priority = priority;
    }

    /**
     * @return the key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey( String key )
    {
        this.key = key;
    }

    /**
     * @return the created
     */
    public Date getCreated()
    {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated( Date created )
    {
        this.created = created;
    }

    /**
     * @return the summary
     */
    public String getSummary()
    {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary( String summary )
    {
        this.summary = summary;
    }

}
