#!/bin/sh

# DESC="ASKDOG Configuration"
NAME=ad-configuration
PIDFILE=/home/$USER/$NAME.pid
RUN_AS=$USER
COMMAND="$JAVA_HOME/bin/java -- -jar $WORKSPACE/${project.artifactId}/target/${project.build.finalName}.jar"

d_start() {
    start-stop-daemon --start --quiet --background --make-pidfile --pidfile $PIDFILE --chuid $RUN_AS --exec $COMMAND
}

d_stop() {
    start-stop-daemon --stop --quiet --pidfile $PIDFILE
    if [ -e $PIDFILE ]
        then rm $PIDFILE
    fi
}

case $1 in
    start)
    echo -n "Starting $NAME"
    d_start
    echo "."
    ;;
    stop)
    echo -n "Stopping $NAME"
    d_stop
    echo "."
    ;;
    restart)
    echo -n "Restarting $NAME"
    d_stop
    sleep 1
    d_start
    echo "."
    ;;
    *)
    echo "usage: $NAME {start|stop|restart}"
    exit 1
    ;;
esac

exit 0

