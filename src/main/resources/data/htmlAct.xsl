<?xml version="1.0"  encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
<xsl:template match="/">

<html>

   <head>
      	<title>Akt</title>
 		<style type="text/css">
    		h1,h2,h3{
    			text-align: center;
    			margin-left: -50px;
    		}   
    		h1,h3{
    			padding: 20px;
			}
    		p{
    			font-size: 20px;
    			font-family: Serif;
    		}  
    		body{
    			margin-left: 50px;
    		}           
        </style>
   </head>

   <body>
      <h1><xsl:value-of select="Akt/@naziv"/></h1>
      <p>
      	Predlagac:<xsl:value-of select="Akt/@predlagac"/><br/>
      	Status:<xsl:value-of select="Akt/@status"/><br/>
      </p>
      <xsl:for-each select="Akt/Deo">
	   		<h2><xsl:value-of select="@naziv"/></h2>
	   		<xsl:for-each select="Glava">
	   			<h2><xsl:value-of select="@naziv"/></h2>
	   			<xsl:for-each select="Odeljak">
	   				<h2><xsl:value-of select="@naziv"/></h2>
	   					<xsl:for-each select="Pododeljak">
	   						<h2><xsl:value-of select="@naziv"/></h2>	
	   						<xsl:for-each select="Clan">
	   							<h3><xsl:value-of select="@naziv"/></h3>
	   							<p><xsl:value-of select="./text()"/></p>
	   							<xsl:for-each select="Stav">
		   							<p>Stav <xsl:value-of select="@id"/>:
		   								<xsl:value-of select="./text()"/></p>
		   							<xsl:for-each select="Tacka">
			   							<p><xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><br/></p>
			   							<xsl:for-each select="Podtacka">
				   							<p style="margin-left: 50px;">(<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><br/></p>
				   							<xsl:for-each select="Alineja">
					   							<p style="margin-left: 100px;">- <xsl:value-of select="./text()"/><br/></p>
					   						</xsl:for-each>
				   						</xsl:for-each>
			   						</xsl:for-each>
		   						</xsl:for-each>
	   						</xsl:for-each>
	   					</xsl:for-each>
	   					<xsl:for-each select="Clan">
	   						<h3><xsl:value-of select="@naziv"/></h3>
   							<p><xsl:value-of select="./text()"/></p>
   							<xsl:for-each select="Stav">
	   							<p>Stav <xsl:value-of select="@id"/>:
	   								<xsl:value-of select="./text()"/></p>
	   							<xsl:for-each select="Tacka">
		   							<p><xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><br/></p>
		   							<xsl:for-each select="Podtacka">
			   							<p style="margin-left: 50px;">(<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><br/></p>
			   							<xsl:for-each select="Alineja">
				   							<p style="margin-left: 100px;">- <xsl:value-of select="./text()"/><br/></p>
				   						</xsl:for-each>
			   						</xsl:for-each>
		   						</xsl:for-each>
	   						</xsl:for-each>
	   					</xsl:for-each>
	   			</xsl:for-each>
	   		</xsl:for-each>
	  </xsl:for-each>


    </body>
</html>
</xsl:template>
</xsl:stylesheet>