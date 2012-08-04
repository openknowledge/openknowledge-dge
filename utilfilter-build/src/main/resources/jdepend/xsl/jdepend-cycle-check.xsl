<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="text" indent="no"/>

  <xsl:template match="/JDepend">
    <xsl:apply-templates select="Cycles/Package"/>
  </xsl:template>

  <xsl:template match="Cycles/Package">
    <xsl:variable name="packageName" select="@Name"/>
    <xsl:variable name="cyclicPackage" select="Package[count(Package)]/text()"/>
    <xsl:text>
      Cycle involving
    </xsl:text>
    <xsl:choose>
      <xsl:when test="$packageName = $cyclicPackage">
        <xsl:for-each select="Package">
          <xsl:text><xsl:value-of select="text()"/>
          </xsl:text>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <xsl:for-each select="Package">
          <xsl:if test="text() = $cyclicPackage">
            <xsl:variable name="startPosition" select="position()"/>
            <xsl:for-each select="//Cycles/Package[@Name=$packageName]/Package">
              <xsl:if test="position() > $startPosition">
                <xsl:text><xsl:value-of select="text()"/>
                </xsl:text>
              </xsl:if>
            </xsl:for-each>
          </xsl:if>
        </xsl:for-each>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>
