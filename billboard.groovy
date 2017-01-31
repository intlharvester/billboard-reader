
def getAvailableCharts() { 
    def availableCharts = new HashMap()
    availableCharts << [
        "hot-100": [
                node : "hot-100",
                name : "Hot 100"
        ]
    ]
    availableCharts << [
        "rock-songs": [
                node : "rock-songs",
                name : "Rock Songs"
        ]
    ]
    availableCharts << [
        "reggae-albums": [
                node : "reggae-albums",
                name : "Raggae Albums"
        ]
    ]
    availableCharts << [
        "jazz-songs": [
                node : "jazz-songs",
                name : "Smooth Jazz Songs"
        ]
    ]
    availableCharts << [
        "jazz-albums": [
                node : "jazz-albums",
                name : "Jazz Albums"
        ]
    ]
    availableCharts << [
        "country-songs": [
                node : "country-songs",
                name : "Country Songs"
        ]
    ]
}

    
def buildYouTubeLink(title, artist) {
    def query = "${title} ${artist}".replaceAll(" ","+")
    "https://www.youtube.com/results?search_query=" + query
}

def getChart(chartParam) {
    def node 
    def name     
    def chartItems = []
    def baseUrl = "http://www.billboard.com/rss/charts/"
    def chart = new HashMap()

    if (getAvailableCharts().containsKey(chartParam)) {
        def dChart = getAvailableCharts()[chartParam]
        node = dChart.node
        name = dChart.name
    } else {
        node = "NA"
        name = "${chartParam} Not Available"
    }

    def url = "${baseUrl}${node}"

    chart << ['node' :  node]
    chart << ['url' : url]
    chart << ['param' : chartParam]
    chart << ['chartName' : name]
    if (!node.equals("NA")) {
        def rss = new XmlSlurper().parse(url) 
        rss.channel.item.each {
            chartItems << [ title: it.title, artist: it.artist, chartTitle: it.chart_item_title]
        }
    }
    chart << ['items' : chartItems]
    chart
}
 
html.html {
    def chart = getChart(params["chart"])
    def name =  "Billboard Chart ${chart.chartName}"

    head {
        title name 
    }
    body {
     
        if (chart.node!="NA") {
	    h1 name
            ul {
                chart.items.each {
                   a(href: buildYouTubeLink(it.chartTitle, it.artist),"${it.title} by $it.artist")
                   br()
                }
            }
        }

        h1 "Available Charts"
        ul {
            getAvailableCharts().each { k,v ->
               a(href: "http://${headers.Host}${request.requestURI}?chart=$k","${v.name}")
               br()
            }
        }

    }
}

