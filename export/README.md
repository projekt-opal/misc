# OPAL Export

Converts RDF to CSV data.


## Examples

```Java
String csv = new Export()

	.addUri("https://www.mcloud.de/export/datasets/4F5E35F6-F128-4948-BB7E-73FA826FDDD0", "RDF/XML")

	.addUri("https://www.mcloud.de/export/datasets/91CDE1D7-306D-4C3C-B7F4-577C36BFC0A2", "RDF/XML")

	.getCsv();
```

* Example input:
[4F5E35F6-F128-4948-BB7E-73FA826FDDD0.rdf](src/test/resources/org/dice_research/opal/export/4F5E35F6-F128-4948-BB7E-73FA826FDDD0.rdf), 
[91CDE1D7-306D-4C3C-B7F4-577C36BFC0A2.rdf](src/test/resources/org/dice_research/opal/export/91CDE1D7-306D-4C3C-B7F4-577C36BFC0A2.rdf)
* Example output: [exported.csv](src/test/resources/org/dice_research/opal/export/exported.csv)
* Example code: [ExportTest.java](src/test/java/org/dice_research/opal/export/ExportTest.java)


## Credits

[Data Science Group (DICE)](https://dice-research.org/) at [Paderborn University](https://www.uni-paderborn.de/)

This work has been supported by the German Federal Ministry of Transport and Digital Infrastructure (BMVI) in the project [Open Data Portal Germany (OPAL)](http://projekt-opal.de/) (funding code 19F2028A).
