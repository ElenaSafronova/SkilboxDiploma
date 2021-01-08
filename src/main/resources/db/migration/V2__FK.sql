
--Description: Adding Foreign Keys

alter table post_comments
    add constraint comments_parent_fk
    foreign key (parent_id) references post_comments (id);

alter table post_comments
    add constraint comments_post_fk
    foreign key (post_id) references posts (id);

alter table post_comments
    add constraint comments_user_fk
    foreign key (user_id) references users (id);

alter table post_votes
    add constraint vote_post_fk
    foreign key (post_id) references posts (id);

alter table post_votes
    add constraint vote_user_fk
    foreign key (user_id) references users (id);

alter table posts
    add constraint post_user_fk
    foreign key (user_id) references users (id);

alter table posts
    add constraint post_moderator_fk
    foreign key (moderator_id) references users (id);

alter table tag2post
    add constraint tag2post_post_fk
    foreign key (post_id) references posts (id);

alter table tag2post
    add constraint tag2post_tag_fk
    foreign key (tag_id) references tags (id);