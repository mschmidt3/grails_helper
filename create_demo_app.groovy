#!/usr/bin/env /opt/groovy/bin/groovy

/**
 * Generates a grails demo application.
 * The grails command must be in PATH.
 */



def appname     = "demo"
if(args.size()>0){
    appname = args[0]
}
def packagename = "com.scmt.${appname}"

// ----------------------------------------------------------------------------
def currentDir = new File(".").getAbsolutePath()
def workingdirname = appname
def packagepath = packagename.replace('.', '/')
def workingdir = ""

println "grails create-app ${appname}".execute().text

workingdir = new File(workingdirname)

println "grails create-domain-class ${packagename}.Book".execute(null, workingdir).text
println "grails create-domain-class ${packagename}.Author".execute(null, workingdir).text

def domainclasses = [
    Book:"""\
package ${packagename}
class Book {
    String  title
    Integer year
    String  genre 
    String  publisher
    BigDecimal price

    static hasMany   = [authors: Author]
    static belongsTo = Author

    static constraints = {
        title  nullable:false, maxSize:64, size:3..64
        year   nullable:true
        genre  nullable:true, maxSize:64
        publisher nullable:true
        price  nullable:true
    }
    
    String toString() { title }
}
""",
    Author:"""\
package ${packagename}
class Author {
    String  name

    static  hasMany = [books: Book]
  
    static constraints = {
        name  nullable:false
    }

    String toString() { name }
}
""",
]


domainclasses.each { key,text ->
    def workfile = new File("${workingdirname}/grails-app/domain/${packagepath}/${key}.groovy")
    workfile.delete()
    workfile << text
}

"grails create-scaffold-controller ${packagename}.Book".execute(null, workingdir)
"grails create-scaffold-controller ${packagename}.Author".execute(null, workingdir)
