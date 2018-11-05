
Jxpacket
=====

Jxpacket is network packet crafting libarary for java.


### How to Use

  - ##### Gradle project
>> Add a dependency to the build.gradle as like below:
>>>
>>> ```
>>> dependencies { 
>>>     compile 'com.ardikars.common:common-net:${common.version}'
>>>     compile 'com.ardikars.jxnet:jxnet-spring-boot-starter:${jxnet.version}'
>>>     compile 'com.ardikars.jxpacket:jxpacket-core:${jxpacket.version}'
>>> }
>>>```
  - ##### Maven project
>> Add a dependency to the pom.xml as like below:
>>>
>>> ```
>>> <dependencies>
>>>     <dependency>
>>>         <groupId>com.ardikars.common</groupId>
>>>         <artifactId>common-net</artifactId>
>>>         <version>${common.version}</version>
>>>     </dependency>
>>>     <dependency>
>>>         <groupId>com.ardikars.jxnet</groupId>
>>>         <artifactId>jxnet-spring-boot-starter</artifactId>
>>>         <version>${jxnet.version}</version>
>>>     </dependency>
>>>     <dependency>
>>>         <groupId>com.ardikars.jxpacket</groupId>
>>>         <artifactId>jxpacket-core</artifactId>
>>>         <version>${jxpacket.version}</version>
>>>     </dependency>
>>> </dependencies>
>>>```

Build Jxnet from Source
=============================

### Build
   - ```./gradlew clean build```
   
### Skip Test
   - ```./gradlew clean build -x test```

Jxpacket dependencies
=====================
  - com.ardikars.common:common-util
  - com.ardikars.common:common-net
  - io.netty:netty-buffer
  
Example
=======
```java

@SpringBootApplication
public class ExampleApplication implements CommandLineRunner {

    @Autowired
    private Context context;

    public static void main(String[] args) {
        register();
        SpringApplication.run(ExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        context.pcapLoop(1000, (user, h, bytes) -> {
            ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(bytes.capacity());
            buffer.setBytes(0, bytes);
            Packet packet = Ethernet.newPacket(buffer);
            packet.forEach(pkt -> System.out.println(pkt.getHeader()));
        }, "");
    }

    private static void register() {
        NetworkLayer.register(NetworkLayer.IPV4, new Ip4.Builder());
        TransportLayer.register(TransportLayer.TCP, new Tcp.Builder());
    }

}

```

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
