#! /bin/bash

set -x

cd /tmp

/usr/bin/mysqld_safe --user=root &

until /usr/bin/mysqladmin -u root status > /dev/null 2>&1; do sleep 1; done

echo "create database search_nibrs_dimensional" | mysql -u root

# Allow other machines in the docker network to connect, but no others
# NOTE: This is a highly unsecure setup.  This is only for demo purposes.  Don't do this outside of a local demo environment!
mysql -u root -e "CREATE USER 'analytics';"
mysql -u root -e "GRANT ALL PRIVILEGES ON search_nibrs_dimensional.* TO 'analytics'@'%';"

if [ "$1" = "Y" ]
then
  echo "Pre-loading pickled dimensional db"
  gunzip search_nibrs_dimensional.sql.gz
  mysql -u root search_nibrs_dimensional < /tmp/search_nibrs_dimensional.sql
  rm -f /tmp/search_nibrs_dimensional.sql
fi

