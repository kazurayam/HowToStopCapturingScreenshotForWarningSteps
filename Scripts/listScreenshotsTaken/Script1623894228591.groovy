import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import com.kms.katalon.core.configuration.RunConfiguration

// identify path of the reports folder
Path reportsFolder = Paths.get(RunConfiguration.getReportFolder())
println(reportsFolder)

// scan the reports folder to look up *.png files in it
List<Path> pngs = Files.list(reportsFolder).filter { p -> 
	p.getFileName().toString().endsWith('.png')
	}.
	collect(Collectors.toList())
assert pngs.size() == 0, "took screenshots:" + pngs

