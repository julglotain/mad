package com.cacf.corporate.mobileappdownloader.bundles.domain;

/**
 * Created by jug on 19/10/2014.
 */
public class ApplicationConfiguration {

    private String title;
    private String version;
    private String desc;

    private FilesURILocations filesURILocations;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public FilesURILocations getFilesURILocations() {
        return filesURILocations;
    }

    public void setFilesURILocations(FilesURILocations filesURILocations) {
        this.filesURILocations = filesURILocations;
    }

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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ApplicationConfiguration{");
        sb.append("title='").append(title).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", desc='").append(desc).append('\'');
        sb.append(", filesURILocations=").append(filesURILocations);
        sb.append('}');
        return sb.toString();
    }

}
