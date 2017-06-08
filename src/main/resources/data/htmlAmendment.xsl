<?xml version="1.0"  encoding="UTF-8"?>
<xsl:stylesheet xmlns:ns2="http://www.parlament.gov.rs/akti" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
<html>

   <head>
      <title>Amandman</title>
      <style type="text/css">
    		h1{
    			text-align: center;
    			margin-left: -50px;
    		}   
    		h1{
    			padding: 20px;
			}
    		p,ul{
    			font-size: 20px;
    		}  
    		body{
    			margin-left: 50px;
    		}           
        </style>
   </head>

   <body>
      <h1 style="text-align: center;"><xsl:value-of select="Amandman/@naziv"/></h1>
      <p>
      	Predlagac:<xsl:value-of select="Amandman/@predlagac"/><br/>
      	Tip:<xsl:value-of select="Amandman/@tip"/><br/>
      	Status:<xsl:value-of select="Amandman/@status"/><br/>
      	Odnosi se na akt:<xsl:value-of select="Amandman/RefAkta"/><br/></p>
      	<h2>Predlozi amandmana:</h2>
      	<xsl:if test="Amandman/@tip='Izmena'">
	      	<ul>
		      	<xsl:for-each select="Amandman/Lista_predloga/Predlog_amandmana">
		      		<li>Zameniti <xsl:value-of select="ns2:ref"/> sledecim tekstom:
		      			<p><xsl:value-of select="sadrzaj"/></p></li>
		   	   	</xsl:for-each>
	      	</ul>
      	</xsl:if>
      	<xsl:if test="Amandman/@tip='Dodavanje'">
	      	<ul>
	      		<xsl:for-each select="Amandman/Lista_predloga/Predlog_amandmana">
			      	<li>Na <xsl:value-of select="ns2:ref"/> dodati:
			      		<p><xsl:value-of select="Cvor/@tipCvora"/>  <xsl:value-of select="Cvor/@id"/>:</p>
			      		<p><xsl:value-of select="Cvor/@naziv"/></p>
				      	<p><xsl:value-of select="Cvor/text()"/></p>
			      		<xsl:for-each select="Cvor/Cvor">
			      			<xsl:if test="@tipCvora='Stav'">
				      			<p>Stav <xsl:value-of select="@id"/> : <xsl:value-of select="./text()"/></p>
					      		<xsl:for-each select="Cvor">
					      			<p><xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><br/></p>
					      			<xsl:for-each select="Cvor">
					      				<p style="margin-left: 50px;">(<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><br/></p>
					      				<xsl:for-each select="Cvor">
					      					<p style="margin-left: 100px;">-<xsl:value-of select="./text()"/><br/></p>
					      				</xsl:for-each>
					      			</xsl:for-each>
					      		</xsl:for-each>
				      		</xsl:if>
				      		<xsl:if test="@tipCvora='Tacka'">
				      			<p><xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/></p>
				      			<xsl:for-each select="Cvor">
				      				<p style="margin-left: 50px;">(<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/></p>
				      				<xsl:for-each select="Cvor">
				      					<p style="margin-left: 100px;">-<xsl:value-of select="./text()"/></p>
				      				</xsl:for-each>
				      			</xsl:for-each>
				      		</xsl:if>
				      		<xsl:if test="@tipCvora='Podtacka'">
				      			<p style="margin-left: 50px;">(<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/></p>
			      				<xsl:for-each select="Cvor">
			      					<p style="margin-left: 100px;">-<xsl:value-of select="./text()"/></p>
			      				</xsl:for-each>
				      		</xsl:if>
				      		<xsl:if test="@tipCvora='Alineja'">
				      			<p style="margin-left: 100px;">-<xsl:value-of select="./text()"/></p>
				      		</xsl:if><p></p>
		      			</xsl:for-each>
			      	</li>
		    	</xsl:for-each>
	    	</ul>
      	</xsl:if>
      	<xsl:if test="Amandman/@tip='Brisanje'">
	      	<ul>
	      		<xsl:for-each select="Amandman/Lista_predloga/Predlog_amandmana">
			      	<li>Obrisati <xsl:value-of select="ns2:ref"/> .</li>
		      	</xsl:for-each>
		    </ul>  	
      	</xsl:if>
      	<h2>Obrazlozenje:</h2>
      	<p><xsl:value-of select="Amandman/Obrazlozenje"/></p>
    </body>
</html>
</xsl:template>
</xsl:stylesheet>