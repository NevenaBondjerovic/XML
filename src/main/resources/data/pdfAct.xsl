<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">
    
    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="act-page">
                    <fo:region-body margin="1in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            
            <fo:page-sequence master-reference="act-page">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-family="sans-serif" font-size="18px" font-weight="bold" padding="10px" text-align="center">
                       	<xsl:value-of select="Akt/@naziv"/>
                    </fo:block>
                    <fo:block font-size="12px" padding="20px">
      					Predlagac:<xsl:value-of select="Akt/@predlagac"/><fo:block/>
      					Status:<xsl:value-of select="Akt/@status"/><fo:block/>
                    </fo:block>
                    <fo:block>
			      	<xsl:for-each select="Akt/Deo">
				   		<fo:block font-size="14px" padding="3px" text-align="center"><xsl:value-of select="@naziv"/></fo:block>
				   		<xsl:for-each select="Glava">
				   			<fo:block font-size="14px" padding="3px" text-align="center"><xsl:value-of select="@naziv"/></fo:block>
				   			<xsl:for-each select="Odeljak">
				   				<fo:block font-size="14px" padding="3px" text-align="center"><xsl:value-of select="@naziv"/></fo:block>
				   				<xsl:for-each select="Pododeljak">
				   						<fo:block font-size="14px" padding="5px" text-align="center"><xsl:value-of select="@naziv"/></fo:block>
				   						<xsl:for-each select="Clan">
				   							<fo:block font-size="14px" padding="10px" text-align="center"><xsl:value-of select="@naziv"/></fo:block>
				   							<fo:block font-size="12px" padding="10px" ><xsl:value-of select="./text()"/></fo:block>
				   							<xsl:for-each select="Stav">
					   							<fo:block font-size="12px" padding="10px" >Stav <xsl:value-of select="@id"/>:
					   								<xsl:value-of select="./text()"/></fo:block>
					   							<xsl:for-each select="Tacka">
						   							<fo:block font-size="12px" text-indent="5mm">
						   								<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/></fo:block>
					   								<xsl:for-each select="Podtacka">
							   							<fo:block font-size="12px" text-indent="10mm">
							   								(<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><br/></fo:block>
							   							<xsl:for-each select="Alineja">
								   							<fo:block font-size="12px" text-indent="15mm">- <xsl:value-of select="./text()"/><br/></fo:block>
								   						</xsl:for-each>
							   						</xsl:for-each>
						   						</xsl:for-each>
					   						</xsl:for-each>
				   						</xsl:for-each>
				   					</xsl:for-each>
				   					<xsl:for-each select="Clan">
				   						<fo:block font-size="14px" padding="10px" text-align="center"><xsl:value-of select="@naziv"/></fo:block>
				   							<fo:block font-size="12px" padding="10px" ><xsl:value-of select="./text()"/></fo:block>
				   							<xsl:for-each select="Stav">
					   							<fo:block font-size="12px" padding="10px" >Stav <xsl:value-of select="@id"/>:
					   								<xsl:value-of select="./text()"/></fo:block>
					   							<xsl:for-each select="Tacka">
						   							<fo:block font-size="12px" text-indent="5mm">
						   								<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/></fo:block>
					   								<xsl:for-each select="Podtacka">
							   							<fo:block font-size="12px" text-indent="10mm">
							   								(<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><br/></fo:block>
							   							<xsl:for-each select="Alineja">
								   							<fo:block font-size="12px" text-indent="15mm">- <xsl:value-of select="./text()"/><br/></fo:block>
								   						</xsl:for-each>
							   						</xsl:for-each>
						   						</xsl:for-each>
					   						</xsl:for-each>
				   					</xsl:for-each>
				   			</xsl:for-each>
				   		</xsl:for-each>
				  	</xsl:for-each>
					</fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>
