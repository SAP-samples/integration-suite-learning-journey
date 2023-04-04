<?xml version="1.0" encoding="UTF-8"?><!--Copyright statement for Type System SAP S/4HANA Cloud SOAP: Copyright © SAP SE 2017. All Rights Reserved. 

See also: https://www.sap.com/corporate/en/legal/copyright/use-of-copyrighted-material.html--><!--Copyright statement for ISO Codelists: Copyright (c) 2017, ISO

All ISO content is copyright protected. The copyright is owned by ISO. Any use of the content, including copying of it in whole or in part, for example to another Internet site, is prohibited and would require written permission from ISO.

All ISO publications are also protected by copyright. The copyright ownership of ISO is clearly indicated on every ISO publication. Any unauthorized use such as copying, scanning or distribution is prohibited.

Requests for permission should be addressed to the ISO Central Secretariat or directly through the ISO member in your country.

See more: https://www.iso.org/privacy-and-copyright.html--><!--Copyright statement for UN/CEFACT Codelists: Copyright (c) United Nations 2000-2008. All rights reserved. None of the materials provided on this web site may be used, reproduced or transmitted, in whole or in part, in any form or by any means, electronic or mechanical, including photocopying, recording or the use of any information storage and retrieval system, except as provided for in the Terms and Conditions of Use of United Nations Web Sites, without permission in writing from the publisher. To request such permission and for further enquiries, contact the Secretary of the Publications Board, United Nations, New York, NY, 10017, USA (pubboard@un.org; Telephone: (+1) 212-963-4664; Facsimile: (+1) 212-963-0077). See also: http://www.unece.org/legal_notice/copyrightnotice.html--><xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" version="2.0"><xsl:output encoding="UTF-8" method="xml" version="1.0"/><xsl:param as="xs:anyURI" name="root-namespace" select="xs:anyURI('http://sap.com/xi/EDI')"/><xsl:template match="/OrderRequest/MessageHeader/ID/*[starts-with(name(), '_CONTENT_')]"><xsl:value-of select="./text()"/></xsl:template><xsl:template match="//*[matches(name(),'_gq[1-3]?_') and not(matches(name(),'_CONTENT*'))]"><xsl:call-template name="Convert_Node"><xsl:with-param name="pNodeName" select="substring-before(name(), '_gq')"/></xsl:call-template></xsl:template><xsl:template match="//*[matches(name(),'_pq[1-3]?_') and not(matches(name(),'_CONTENT*'))]"><xsl:call-template name="Convert_Node"><xsl:with-param name="pNodeName" select="substring-before(name(), '_pq')"/></xsl:call-template></xsl:template><xsl:template match="@*[matches(name(),'_pq[1-3]?_') and not(matches(name(),'_CONTENT*'))]"><xsl:call-template name="Convert_Attr"><xsl:with-param name="pAttrName" select="substring-before(name(), '_pq')"/></xsl:call-template></xsl:template><xsl:template name="Convert_Node"><xsl:param name="pNodeName"/><xsl:if test="$pNodeName != ''"><xsl:element name="{$pNodeName}"><xsl:apply-templates select="@*|node()"/></xsl:element></xsl:if></xsl:template><xsl:template name="Convert_Attr"><xsl:param name="pAttrName"/><xsl:if test="$pAttrName != ''"><xsl:attribute name="{$pAttrName}"><xsl:value-of select="."/></xsl:attribute></xsl:if></xsl:template><xsl:template match="@*|node()"><xsl:copy><xsl:apply-templates select="@*|node()"/></xsl:copy></xsl:template><xsl:template match="/*"><xsl:element name="ns1:{local-name()}" namespace="{$root-namespace}"><xsl:apply-templates select="@*|node()"/></xsl:element></xsl:template></xsl:stylesheet>