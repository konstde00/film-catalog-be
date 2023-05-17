alter table films_collections
alter column id SET DEFAULT
  nextval('films_collections_id_seq')