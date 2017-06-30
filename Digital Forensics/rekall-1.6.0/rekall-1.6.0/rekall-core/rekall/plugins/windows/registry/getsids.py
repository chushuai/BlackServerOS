# Rekall Memory Forensics
# Copyright (C) 2008 Volatile Systems
# Copyright (C) 2007, 2008 Brendan Dolan-Gavitt bdolangavitt@wesleyan.edu
# http://moyix.blogspot.com
# Copyright 2013 Google Inc. All Rights Reserved.
#
# Additional Authors:
# Mike Auty <mike.auty@gmail.com>
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or (at
# your option) any later version.
#
# This program is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
# General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
#
# Based heavily upon the getsids plugin by Moyix
# http://kurtz.cs.wesleyan.edu/%7Ebdolangavitt/memory/getsids.py

"""
@author:       AAron Walters and Brendan Dolan-Gavitt
@license:      GNU General Public License 2.0 or later
@contact:      awalters@volatilesystems.com,bdolangavitt@wesleyan.edu
@organization: Volatile Systems
"""
# pylint: disable=protected-access

import re
from rekall.plugins.windows import common


class GetSIDs(common.WinProcessFilter):
    """Print the SIDs owning each process token."""

    __name = "tokens"

    well_known_sid_re = [
      (re.compile(r'S-1-5-[0-9-]+-500'), 'Administrator'),
      (re.compile(r'S-1-5-[0-9-]+-501'), 'Guest'),
      (re.compile(r'S-1-5-[0-9-]+-502'), 'KRBTGT'),
      (re.compile(r'S-1-5-[0-9-]+-512'), 'Domain Admins'),
      (re.compile(r'S-1-5-[0-9-]+-513'), 'Domain Users'),
      (re.compile(r'S-1-5-[0-9-]+-514'), 'Domain Guests'),
      (re.compile(r'S-1-5-[0-9-]+-515'), 'Domain Computers'),
      (re.compile(r'S-1-5-[0-9-]+-516'), 'Domain Controllers'),
      (re.compile(r'S-1-5-[0-9-]+-517'), 'Cert Publishers'),
      (re.compile(r'S-1-5-[0-9-]+-520'), 'Group Policy Creator Owners'),
      (re.compile(r'S-1-5-[0-9-]+-533'), 'RAS and IAS Servers'),
      (re.compile(r'S-1-5-5-[0-9]+-[0-9]+'), 'Logon Session'),
      (re.compile(r'S-1-5-21-[0-9-]+-518'), 'Schema Admins'),
      (re.compile(r'S-1-5-21-[0-9-]+-519'), 'Enterprise Admins'),
      (re.compile(r'S-1-5-21-[0-9-]+-553'), 'RAS Servers'),
    ]

    well_known_sids = {
      'S-1-0': 'Null Authority',
      'S-1-0-0': 'Nobody',
      'S-1-1': 'World Authority',
      'S-1-1-0': 'Everyone',
      'S-1-2': 'Local Authority',
      'S-1-2-0': 'Local (Users with the ability to log in locally)',
      'S-1-2-1': ('Console Logon (Users who are logged onto the physical '
                  'console)'),
      'S-1-3': 'Creator Authority',
      'S-1-3-0': 'Creator Owner',
      'S-1-3-1': 'Creator Group',
      'S-1-3-2': 'Creator Owner Server',
      'S-1-3-3': 'Creator Group Server',
      'S-1-3-4': 'Owner Rights',
      'S-1-4': 'Non-unique Authority',
      'S-1-5': 'NT Authority',
      'S-1-5-1': 'Dialup',
      'S-1-5-2': 'Network',
      'S-1-5-3': 'Batch',
      'S-1-5-4': 'Interactive',
      'S-1-5-6': 'Service',
      'S-1-5-7': 'Anonymous',
      'S-1-5-8': 'Proxy',
      'S-1-5-9': 'Enterprise Domain Controllers',
      'S-1-5-10': 'Principal Self',
      'S-1-5-11': 'Authenticated Users',
      'S-1-5-12': 'Restricted Code',
      'S-1-5-13': 'Terminal Server Users',
      'S-1-5-14': 'Remote Interactive Logon',
      'S-1-5-15': 'This Organization',
      'S-1-5-17': 'This Organization (Used by the default IIS user)',
      'S-1-5-18': 'Local System',
      'S-1-5-19': 'NT Authority',
      'S-1-5-20': 'NT Authority',
      'S-1-5-32-544': 'Administrators',
      'S-1-5-32-545': 'Users',
      'S-1-5-32-546': 'Guests',
      'S-1-5-32-547': 'Power Users',
      'S-1-5-32-548': 'Account Operators',
      'S-1-5-32-549': 'Server Operators',
      'S-1-5-32-550': 'Print Operators',
      'S-1-5-32-551': 'Backup Operators',
      'S-1-5-32-552': 'Replicators',
      'S-1-5-32-554': 'BUILTIN\\Pre-Windows 2000 Compatible Access',
      'S-1-5-32-555': 'BUILTIN\\Remote Desktop Users',
      'S-1-5-32-556': 'BUILTIN\\Network Configuration Operators',
      'S-1-5-32-557': 'BUILTIN\\Incoming Forest Trust Builders',
      'S-1-5-32-558': 'BUILTIN\\Performance Monitor Users',
      'S-1-5-32-559': 'BUILTIN\\Performance Log Users',
      'S-1-5-32-560': 'BUILTIN\\Windows Authorization Access Group',
      'S-1-5-32-561': 'BUILTIN\\Terminal Server License Servers',
      'S-1-5-32-562': 'BUILTIN\\Distributed COM Users',
      'S-1-5-32-568': 'BUILTIN\\IIS IUSRS',
      'S-1-5-32-569': 'Cryptographic Operators',
      'S-1-5-32-573': 'BUILTIN\\Event Log Readers',
      'S-1-5-32-574': 'BUILTIN\\Certificate Service DCOM Access',
      'S-1-5-33': 'Write Restricted',
      'S-1-5-64-10': 'NTLM Authentication',
      'S-1-5-64-14': 'SChannel Authentication',
      'S-1-5-64-21': 'Digest Authentication',
      'S-1-5-80': 'NT Service',
      'S-1-5-86-1544737700-199408000-2549878335-3519669259-381336952': (
          'WMI (Local Service)'),
      'S-1-5-86-615999462-62705297-2911207457-59056572-3668589837': (
          'WMI (Network Service)'),
      'S-1-5-1000': 'Other Organization',
      'S-1-16-0': 'Untrusted Mandatory Level',
      'S-1-16-4096': 'Low Mandatory Level',
      'S-1-16-8192': 'Medium Mandatory Level',
      'S-1-16-8448': 'Medium Plus Mandatory Level',
      'S-1-16-12288': 'High Mandatory Level',
      'S-1-16-16384': 'System Mandatory Level',
      'S-1-16-20480': 'Protected Process Mandatory Level',
      'S-1-16-28672': 'Secure Process Mandatory Level',
    }

    table_header = [
        dict(name="Process", width=40),
        dict(name="Sid", width=50),
        dict(name="Comment")
    ]

    def column_types(self):
        return dict(
            Process=self.session.profile._EPROCESS(),
            Pid=0,
            Sid="",
            Comment="")

    def find_sid_re(self, sid_string):
        for reg, name in self.well_known_sid_re:
            if reg.search(sid_string):
                return name

    def collect(self):
        # First enumerate all the users on this system.
        users = {}
        for _, v, f in self.session.plugins.users().GenerateUsers():
            users[f.Rid] = v.UserName.Value

        for task in self.filter_processes():
            for sa in task.Token.UserAndGroups:
                # The RID is the last SubAuthority
                rid = sa.Sid.SubAuthority[sa.Sid.SubAuthorityCount-1]
                sid_name = ""
                username = users.get(rid)
                if username:
                    sid_name = "User: %s" % username

                sid_string = unicode(sa.Sid.deref())
                if sid_string in self.well_known_sids:
                    sid_name = self.well_known_sids[sid_string]
                else:
                    sid_name_re = self.find_sid_re(sid_string)
                    if sid_name_re:
                        sid_name = sid_name_re

                yield(task, sid_string, sid_name)
