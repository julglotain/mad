Configuration
==================

* Copier les différents fichiers de /conf-sample et les placer dans un répertoire
* Editer les propriétées du fichier mad-config.properties et renseigner les paths des fichiers de configuration apps-bundle.xml et users-store.properties
* Editer les propriétés des fichiers de configuration apps-store.xml et users-store.properties pour une configuration initiale
* Lancer le daemon Tomcat avec la variable d'environnement -Dmad.config.file=/path/to/mad-config.properties


User configuration sample
------------------------

    ####################################################################
    #
    #   ** GLOBAL **
    #
    #   * Usernames are store under 'mad.user' path properties
    #   * and case-sensitive ..
    #   * Password is hashed with MD5
    #   * Bundles prefixes names are referenced for each user
    #
    #
    ####################################################################

    ####################################################################
    # 'admin' user
    #
    #   * A ne modifier sous aucun pretexte
    #   * Possede les droits d'acces sur tous les profiles et bundles
    #
    ####################################################################
    mad.user.admin.userpassword=44a919f32c365cd45625efdeacd80c40
    mad.user.admin.profiles=ALL
    mad.user.admin.bundles=ALL
    mad.user.admin.authorities=ADMIN,DOWNLOAD

    ####################################################################
    # 'app01' user
    #
    #   * 'app01' has access to bundle prefixed by com.foo.app01 and com.lorem,
    #     for example com.foo.app01.dev or com.lorem.app
    #   * 'app01' has access to the bundles with a profile DEV or RCT
    #   * He can only download apps, no admin access
    #
    ####################################################################
    mad.user.app01.userpassword=44a919f32c365cd45625efdeacd80c40
    mad.user.app01.profiles=DEV,RCT
    mad.user.app01.bundles=com.foo.app01, com.lorem
    mad.user.app01.authorities=DOWNLOAD

    ####################################################################
    # User app02
    ####################################################################
    mad.user.app02.userpassword=44a919f32c365cd45625efdeacd80c40
    mad.user.app02.profiles=PROD
    mad.user.app02.bundles=com.foo.app02
    mad.user.app02.authorities=DOWNLOAD
