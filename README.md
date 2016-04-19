# fb-groups

## Data
Using Facebook Graph API v2.3 one is able to acquire data from an User's group.
Of course this is possible only if you have being granted the _user\_groups_ permission. 
Anyways, since Graph API v2.4 the [_user\_groups_ permission is deprecated](https://developers.facebook.com/docs/apps/changelog#v2_4_deprecations). This deprecation means you can only get data from a group managed by the user, through _user\_managed_groups_ permission.
But good news, we can use v2.3 until [July 8, 2017](https://developers.facebook.com/docs/apps/changelog#versions).

### Data acquired
By doing a GET request to [_/v2.3/{group-id}/members_](https://developers.facebook.com/docs/graph-api/reference/v2.3/group/members) is possible to obtain all users of the group. Data acquired:
* User id.
* The person's full name. 

By doing a GET request to [_/v2.3/{group-id}/feed_](https://developers.facebook.com/docs/graph-api/reference/v2.3/group/feed) is possible to obtain a group's feed. This feed is an array of [Posts](https://developers.facebook.com/docs/graph-api/reference/post). By performing some test I was able to identify Posts in the group's feed as:
* Photos
* Text

Each post has the following fields:
* Comments: Each comment has its content and the user who commented. As well as the total count of likes received in the comment.
* Likes: A like has the user who performed it.
* Message: Text content.
* Tags: 
 * _with\_tags_ : Users tagged as being 'with' the Post's author.
 * _story\_tags_: Users tagged in the Post's text message.

**Right now the text content of both Posts and Comments are not being stored or analyzed.**

### Known limitations
#### Data
The following interactions are not being imported right now:
* Comment's replies (nested comments) and its likes
* Likes on a comment.
To retrieve a Comment's replies one have to perform an API request for each comment. The same goes for retrieving the users who liked a Comment.

#### Model
What is being modeled as a graph are interactions between users. Interactions from an User to the Group is not being considered.

Example: When an User posts something to the group, and this post does not tag anyone, does not have comments and likes, this post will not yield any interaction, therefore no changes will be made to the graph. 

## PageRank
Implementations based completely on [Chapter 14 of the Book _Networks, Crowds, and Markets: Reasoning about a Highly Connected World._ by David Easley and John Kleinberg](http://www.cs.cornell.edu/home/kleinber/networks-book/networks-book-ch14.pdf). Specifically, the implementation is based on repeated improvement, as described in the chapter.

One little addition, an obvious one but a modification nonetheless, was to consider that the graph is a multigraph. So to figure out how much of PageRank share a neighbor would receive, the number of interactions to this neighbor was considered.
Consider a graph with _Node A_, having three edges to _Node B_, and one edge to _Node C_. Then _Node 'B'_ would receive 3/4 of _Node A_'s PageRank, and _Node 'C'_ would receive 1/4.

The graph already did not have self-loops, so no problems with this.
