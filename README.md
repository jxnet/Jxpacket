
Jxpacket
=====

Jxpacket is packet crafting libarary for java.


### How to Use

  - ##### Gradle project
>> Add a dependency to the build.gradle as like below:
>>>
>>> ```
>>> dependencies { 
>>>     compile 'com.ardikars.jxpacket:jxpacket-core:1.0.0'
>>> }
>>>```
  - ##### Maven project
>> Add a dependency to the pom.xml as like below:
>>>
>>> ```
>>> <dependencies>
>>>     <dependency>
>>>         <groupId>com.ardikars.jxpacket</groupId>
>>>         <artifactId>jxpacket-core</artifactId>
>>>         <version>1.0.0</version>
>>>     </dependency>
>>> </dependencies>
>>>```

Build Jxnet from Source
=============================

### Build
   - ```./mvnw clean package```
   
### Skip Test
   - ```./mvnw clean package -DskipTests```

Jxpacket dependencies
==================
  - com.ardikars.common:common-util
  - com.ardikars.common:common-net
  - io.netty:netty-buffer

License
=======

GNU Lesser General Public License, Version 3

```
/**
 * Copyright (C) 2017-2018  Ardika Rommy Sanjaya <contact@ardikars.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
```

Contact
=======

Email: Ardika Rommy Sanjaya (contact@ardikars.com)


Issues
======

Have a bug? Please create an issue here on GitHub!

https://github.com/jxnet/Jxpacket/issues
