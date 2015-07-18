Spine
=====

Usage
=====

    $ git clone git@github.com:varl/spine.git && cd spine && gradle build && gradle shadowJar
    $ java -jar build/libs/spine-1.0-SNAPSHOT-all.jar --port=8003 --remote=https://github.com/varl/vlv-spine.git

Set up a reverse proxy to your port, maybe start the progam with supervisor,
and you are done.

Check out the structure used in [vlv-spine](https://github.com/varl/vlv-spine)
for content, templates and static artifacts.

/v.
