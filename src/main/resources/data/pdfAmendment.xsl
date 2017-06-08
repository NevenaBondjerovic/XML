<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ns2="http://www.parlament.gov.rs/akti" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">
    
    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="amendment-page">
                    <fo:region-body margin="1in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            
            <fo:page-sequence master-reference="amendment-page">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-family="sans-serif" font-size="18px" font-weight="bold" padding="10px" text-align="center">
                       	<xsl:value-of select="Amandman/@naziv"/>
                    </fo:block>
                    <fo:block font-size="12px" padding="20px">
      					Predlagac:<xsl:value-of select="Amandman/@predlagac"/><fo:block/>
      					Tip:<xsl:value-of select="Amandman/@tip"/><fo:block/>
      					Status:<xsl:value-of select="Amandman/@status"/><fo:block/>
      					Odnosi se na akt:<xsl:value-of select="Amandman/RefAkta"/><fo:block/>
                    </fo:block>
                    <fo:block padding="10px">
						<fo:inline font-size="16px" font-weight="bold">Predlozi amandmana:</fo:inline>
						<xsl:if test="Amandman/@tip='Izmena'">
					   	   	<fo:list-block padding="10px">
						      <xsl:for-each select="Amandman/Lista_predloga/Predlog_amandmana">
						      	<fo:list-item  padding="3px">
									 <fo:list-item-label>
									   <fo:block>*</fo:block>
									 </fo:list-item-label>
									 <fo:list-item-body>
								      	<fo:block text-indent="5mm">Zameniti <xsl:value-of select="ns2:ref"/> sledecim tekstom:<fo:block/>
								      		<fo:block text-indent="5mm"><xsl:value-of select="sadrzaj"/><fo:block/></fo:block>
								      	</fo:block>
								     </fo:list-item-body>
								 </fo:list-item>
						     </xsl:for-each>
					      	</fo:list-block>
					      	</xsl:if>
					      	<xsl:if test="Amandman/@tip='Dodavanje'">
						   	   	<fo:list-block>
						      		<xsl:for-each select="Amandman/Lista_predloga/Predlog_amandmana">
								      	<fo:list-item  padding="10px">
											 <fo:list-item-label>
											   <fo:block>*</fo:block>
											 </fo:list-item-label>
											 <fo:list-item-body>
											 <fo:block>
										      	Na <xsl:value-of select="ns2:ref"/> dodati:<fo:block/>
									      		<xsl:value-of select="Cvor/@tipCvora"/>  <xsl:value-of select="Cvor/@id"/>: 
									      		<xsl:value-of select="Cvor/@naziv"/><fo:block/>
										      	<xsl:value-of select="Cvor/text()"/><fo:block/>
									      		<xsl:for-each select="Cvor/Cvor">
									      			<xsl:if test="@tipCvora='Stav'">
										      			Stav <xsl:value-of select="@id"/> : <fo:block/>
										      			<xsl:value-of select="./text()"/><fo:block/>
											      		<xsl:for-each select="Cvor">
											      			<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><fo:block/>
											      			<xsl:for-each select="Cvor">
											      				<fo:block text-indent="5mm">(<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/></fo:block>
											      				<xsl:for-each select="Cvor">
											      					<fo:block text-indent="10mm">-<xsl:value-of select="./text()"/><fo:block/></fo:block>
											      				</xsl:for-each>
											      			</xsl:for-each>
											      		</xsl:for-each>
										      		</xsl:if>
										      		<xsl:if test="@tipCvora='Tacka'">
										      			<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><fo:block/>
										      			<xsl:for-each select="Cvor">
										      				(<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><fo:block/>
										      				<xsl:for-each select="Cvor">
										      					-<xsl:value-of select="./text()"/><fo:block/>
										      				</xsl:for-each>
										      			</xsl:for-each>
										      		</xsl:if>
										      		<xsl:if test="@tipCvora='Podtacka'">
										      			(<xsl:value-of select="@id"/>) <xsl:value-of select="./text()"/><fo:block/>
									      				<xsl:for-each select="Cvor">
									      					-<xsl:value-of select="./text()"/><fo:block/>
									      				</xsl:for-each>
										      		</xsl:if>
										      		<xsl:if test="@tipCvora='Alineja'">
										      			-<xsl:value-of select="./text()"/><fo:block/>
										      		</xsl:if><p></p>
								      			</xsl:for-each>
										      </fo:block>
										      </fo:list-item-body>
										 </fo:list-item>
								    </xsl:for-each>
							    </fo:list-block>
					      	</xsl:if>
					      	<xsl:if test="Amandman/@tip='Brisanje'">
					      	<fo:list-block padding="10px">
					      		<xsl:for-each select="Amandman/Lista_predloga/Predlog_amandmana">
							      	<fo:list-item  padding="3px">
										 <fo:list-item-label>
										   <fo:block>*</fo:block>
										 </fo:list-item-label>
										 <fo:list-item-body>
									      	<fo:block text-indent="5mm">Obrisati <xsl:value-of select="ns2:ref"/>.</fo:block>
									     </fo:list-item-body>
									 </fo:list-item>
							    </xsl:for-each>
					      	</fo:list-block>
   							</xsl:if>               
      					</fo:block>
      					<fo:block padding="10px">
      						<fo:inline font-size="16px" font-weight="bold">Obrazlozenje:</fo:inline>
							<fo:block text-indent="5mm" padding="10px"><xsl:value-of select="Amandman/Obrazlozenje"/></fo:block>
      					</fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>
