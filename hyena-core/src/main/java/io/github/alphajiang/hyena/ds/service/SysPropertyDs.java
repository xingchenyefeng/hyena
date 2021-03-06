/*
 *  Copyright (C) 2019 Alpha Jiang. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.github.alphajiang.hyena.ds.service;

import io.github.alphajiang.hyena.ds.mapper.SysPropertyMapper;
import io.github.alphajiang.hyena.model.po.SysPropertyPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class SysPropertyDs {
    private static final String KEY_SYS_VERSION = "sql_version";

    @Autowired
    private SysPropertyMapper sysPropertyMapper;

    public int getSqlVersion() {
        SysPropertyPo sysProp = this.getSysProperty(KEY_SYS_VERSION);
        return sysProp == null ? 0 : Integer.parseInt(sysProp.getValue());
    }

    @Transactional
    public void setSqlVersion(int sqlVersion) {
        SysPropertyPo sysProp = this.getSysProperty(KEY_SYS_VERSION);
        if (sysProp == null) {
            sysProp = new SysPropertyPo();
            sysProp.setKey(KEY_SYS_VERSION).setValue(String.valueOf(sqlVersion)).setEnable(true);
            this.sysPropertyMapper.insertOrUpdate(sysProp);
        } else if (sysProp.getValue().equals(String.valueOf(sqlVersion))) {
            log.info("current sql version is {}, ignore set operation", sysProp.getValue());
        }else {
            this.sysPropertyMapper.updateSysProperty(sysProp.getKey(), String.valueOf(sqlVersion));
        }
    }


    public SysPropertyPo getSysProperty(String key) {
        return this.sysPropertyMapper.getSysProperty(key);
    }

    public void createSysPropertyTable() {
        this.sysPropertyMapper.createSysPropertyTable();
    }
}
