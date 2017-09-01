
    drop table if exists t_activity;

    drop table if exists t_agreement;

    drop table if exists t_art_team;

    drop table if exists t_banner;

    drop table if exists t_fans;

    drop table if exists t_like_star;

    drop table if exists t_rember_me_token;

    drop table if exists t_sms_code;

    drop table if exists t_sys_img;

    drop table if exists t_uploaded_video;

    drop table if exists t_user;

    drop table if exists t_user_album;

    drop table if exists t_user_img;

    drop table if exists t_user_joined_activity;

    drop table if exists t_user_request;

    drop table if exists t_video_comment;

    drop table if exists t_video_upload_token;

    create table t_activity (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        contact varchar(255) not null,
        detail varchar(255) not null,
        end_date datetime not null,
        img_url varchar(255) not null,
        location varchar(255),
        name varchar(255) not null,
        publisher_id bigint not null,
        start_date datetime not null,
        primary key (id)
    );

    create table t_agreement (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        content varchar(255) not null,
        title varchar(255) not null,
        primary key (id)
    );

    create table t_art_team (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        detail varchar(255),
        fans_amount integer not null,
        img_url varchar(255) not null,
        leader_id bigint,
        like_amount integer not null,
        location varchar(255),
        name varchar(255) not null,
        primary key (id)
    );

    create table t_banner (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        display bit not null,
        link_url varchar(255),
        position integer not null,
        title varchar(255) not null,
        url varchar(255),
        primary key (id)
    );

    create table t_fans (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        category integer not null,
        fans_id bigint not null,
        team_id bigint,
        user_id bigint,
        primary key (id)
    );

    create table t_like_star (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        type varchar(255) not null,
        user_id bigint not null,
        video_id bigint not null,
        primary key (id)
    );

    create table t_rember_me_token (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        expire_at datetime not null,
        token varchar(255) not null,
        user_id bigint not null,
        primary key (id)
    );

    create table t_sms_code (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        expire_date datetime not null,
        phone varchar(255) not null,
        sms_code varchar(255) not null,
        primary key (id)
    );

    create table t_sys_img (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        bucket varchar(255) not null,
        expire_time bigint,
        origin_name varchar(255),
        status integer not null,
        stored_key varchar(255) not null,
        upload_complete bit not null,
        primary key (id)
    );

    create table t_uploaded_video (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        category integer not null,
        like_amount integer,
        name varchar(255),
        owner_team_id bigint,
        thumbnail_url varchar(255),
        upload_user_id bigint,
        url varchar(255),
        uvt_id bigint not null,
        visit_amount integer,
        primary key (id)
    );

    create table t_user (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        address varchar(255),
        fans_amount integer not null,
        follow_amount integer not null,
        is_disabled bit not null,
        is_leader bit not null,
        name varchar(255) not null,
        phone varchar(255) not null,
        role integer not null,
        team_id bigint,
        user_image varchar(255) not null,
        primary key (id)
    );

    create table t_user_album (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        name varchar(255),
        user_id bigint,
        primary key (id)
    );

    create table t_user_img (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        bucket varchar(255) not null,
        expire_time bigint,
        origin_name varchar(255),
        status integer not null,
        stored_key varchar(255) not null,
        upload_complete bit not null,
        album_id bigint not null,
        user_id bigint not null,
        primary key (id)
    );

    create table t_user_joined_activity (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        activity_id bigint not null,
        user_id bigint not null,
        primary key (id)
    );

    create table t_user_request (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        request_time datetime not null,
        user_id bigint not null,
        primary key (id)
    );

    create table t_video_comment (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        comment varchar(255) not null,
        is_valid bit not null,
        user_id bigint not null,
        video_id bigint not null,
        primary key (id)
    );

    create table t_video_upload_token (
        id bigint not null auto_increment,
        created_at datetime not null,
        last_update_time datetime not null,
        version integer not null,
        bucket varchar(255) not null,
        expire_time bigint,
        origin_name varchar(255),
        status integer not null,
        stored_key varchar(255) not null,
        upload_complete bit not null,
        audit_user_id bigint,
        ext varchar(255),
        file_hash varchar(255),
        owner_team_id bigint,
        title varchar(255) not null,
        user_id bigint not null,
        primary key (id)
    );

    alter table t_art_team 
        add constraint UK_a08e4rgqh7dounigfbj6sxutm unique (name);

    alter table t_fans 
        add constraint UK_qwnji9ajqff12wltcfle0ijya unique (fans_id, team_id, user_id);

    alter table t_sys_img 
        add constraint UK_7pogfd4mbmhcky6mxxemdxgqp unique (bucket, stored_key);

    alter table t_uploaded_video 
        add constraint UK_j7o4v2c9un06kkoy64qiicd64 unique (uvt_id);

    alter table t_user 
        add constraint UK_m5bu5erj83eubjsa1nyms0t89 unique (phone);

    alter table t_user_album 
        add constraint UK_gtyysm0twc57jo29xvu2b7fal unique (name, user_id);

    alter table t_user_img 
        add constraint UK_syfkd4mrm2bcstxghtw1jdy0n unique (bucket, stored_key);

    alter table t_video_upload_token 
        add constraint UK_fb5xukbxdri4og64myfkv5ch0 unique (bucket, stored_key);
