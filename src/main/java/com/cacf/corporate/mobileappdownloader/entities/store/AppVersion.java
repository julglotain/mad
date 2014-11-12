package com.cacf.corporate.mobileappdownloader.entities.store;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by jug on 06/11/2014.
 */
@XStreamAlias("version")
public class AppVersion implements Comparable<AppVersion> {

    // version number of the app
    @XStreamAsAttribute
    private String number;

    // name of the app
    private String name;

    // description of the app
    @XStreamAlias("desc")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FilesURILocations getFilesURILocations() {
        return filesURILocations;
    }

    public void setFilesURILocations(FilesURILocations filesURILocations) {
        this.filesURILocations = filesURILocations;
    }

    private FilesURILocations filesURILocations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int compareTo(AppVersion o) {
        return this.number.compareTo(o.getNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppVersion that = (AppVersion) o;

        if (!name.equals(that.name)) return false;
        if (!number.equals(that.number)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = number.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    /**
     *
     */
    public static class FilesURILocations {

        private String app;
        private Icons icons;

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public Icons getIcons() {
            return icons;
        }

        public void setIcons(Icons icons) {
            this.icons = icons;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("FilesURILocations{");
            sb.append("app='").append(app).append('\'');
            sb.append(", icons=").append(icons);
            sb.append('}');
            return sb.toString();
        }

        public static class Icons {
            private String small;

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            private String large;

            @Override
            public String toString() {
                final StringBuffer sb = new StringBuffer("Icons{");
                sb.append("small='").append(small).append('\'');
                sb.append(", large='").append(large).append('\'');
                sb.append('}');
                return sb.toString();
            }
        }

    }
}
