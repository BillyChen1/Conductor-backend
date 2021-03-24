# 指挥系统Java后端

# 接口文档

[TOC]


- 注意： 标有星号的接口可能会在后台管理系统中用到

## 1. 校验当前队员是否在已注册的队员当中

##### 简要描述

- 校验当前队员是否在已注册的队员当中。在服务端的数据库中保存了已经注册的队员信息。队员登录时，会校验当前用户的微信号是否已经在后台管理系统中注册

##### 请求URL

- ` http://xx.com/user/login`

##### 请求方式

- GET

##### 参数

| 参数名 | 必选 | 类型   | 说明             |
| :----- | :--- | :----- | ---------------- |
| wxId   | 是   | string | 当前队员的微信号 |

##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": {
      "uid": "1",
      "username": "张三",
      "phone": "13000000001",
      "age": 20,
      "gender": "男" ,
      "address": "武汉市洪山区珞喻路129号武汉大学信息学部",
      "role": 1
    }
  }
```

##### 返回参数说明 

| 参数名   | 类型   | 说明                           |
| :------- | :----- | ------------------------------ |
| uid      | string | 用户id                         |
| username | string | 队员姓名                       |
| phone    | string | 队员电话号码                   |
| age      | number | 队员年龄                       |
| gender   | string | 队员性别                       |
| address  | string | 队员家庭住址                   |
| role     | number | 用户角色，0为队员，1为普通用户 |

##### 备注 

- 如果队员不在已注册列表中，则说明角色为普通用户。



## 2. 获取一名用户的基本信息

##### 简要描述

- 根据用户id获取用户的基本信息

##### 请求URL

- ` http://xx.com/user/check/{uid}`

##### 请求方式

- GET

##### 参数

##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": {
      "uid": "1",
      "username": "张三",
      "phone": "13000000001",
      "age": 20,
      "gender": "男" ,
      "address": "武汉市洪山区珞喻路129号武汉大学信息学部",
      "role":0
    }
  }
```

##### 返回参数说明 

| 参数名   | 类型   | 说明                           |
| :------- | :----- | ------------------------------ |
| uid      | string | 用户id                         |
| username | string | 队员姓名                       |
| phone    | string | 队员电话号码                   |
| age      | number | 队员年龄                       |
| gender   | string | 队员性别                       |
| address  | string | 队员家庭住址                   |
| role     | number | 用户角色，0为队员，1为普通用户 |

##### 备注 

- 如果队员不在已注册列表中，则说明角色为普通用户。


## 3. 更新一个队员的地理位置

##### 简要描述

- 更新指定队员的地理位置。在小程序启动时调用该接口。

##### 请求URL

- ` http://xx.com/user/updateLocation `

##### 请求方式

- POST

##### 参数

| 参数名    | 必选 | 类型   | 说明         |
| :-------- | :--- | :----- | ------------ |
| uid       | 是   | string | 队员id       |
| latitude  | 是   | number | 队员所在纬度 |
| longitude | 是   | number | 队员所在经度 |


##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": {
    }
  }
```


## 4. 家属报案（发出一条救援请求）

##### 简要描述

- 家属通过此接口发布报案信息

##### 请求URL

- ` http://xx.com/task/submit `

##### 请求方式

- POST

##### 参数

| 参数名      | 必选 | 类型   | 说明                      |
| :---------- | :--- | :----- | ------------------------- |
| lostName    | 是   | string | 走失者姓名                |
| lostBirth   | 是   | string | 走失者出生年月 yyyy-MM-dd |
| lostGender  | 是   | string | 走失者性别                |
| photo       | 是   | string | 走失者的图片url           |
| latitude    | 是   | number | 走失地点的纬度            |
| longitude   | 是   | number | 走失地点的经度            |
| lostAddress | 否   | string | 走失者的家庭住址          |
| lostPhone   | 否   | string | 家属的联系方式            |
| detail      | 否   | string | 补充信息                  |

##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": {
    }
  }
```

## 5. 家属报案后对信息修改编辑（暂时废弃）

##### 简要描述

- 家属通过此接口修改报案信息

##### 请求URL

- ` http://xx.com/task/submit/{requestId} `

##### 请求方式

- POST

##### 参数

| 参数名      | 必选 | 类型   | 说明                      |
| :---------- | :--- | :----- | ------------------------- |
| lostName    | 是   | string | 走失者姓名                |
| lostBirth   | 是   | string | 走失者出生年月 yyyy-MM-dd |
| lostGender  | 是   | string | 走失者性别                |
| photo       | 是   | string | 走失者的图片url           |
| latitude    | 是   | number | 走失地点的纬度            |
| longitude   | 是   | number | 走失地点的经度            |
| lostAddress | 否   | string | 走失者的家庭住址          |
| lostPhone   | 否   | string | 家属的联系方式            |
| detail      | 否   | string | 补充信息                  |

##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": {
    }
  }
```

## **6. 查看一条报案信息详情（救援请求）

##### 简要描述

- 根据指定id获取一条特定的报案信息详情

##### 请求URL

- ` http://xx.com/task/{requestId} `

##### 请求方式

- GET


##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": {
		"requestId":"1",
		"lostName":"张三",
		"lostAge":70,
		"lostGender":"男",
		"photo":"https://xxxxx",
		"latitude":31.22,
		"longitude": 113.00,
		"lostAddress": "湖北省武汉市青山区翠园社区xxxx",
		"lostPhone": "13000000002",
		"detail":"走失时穿着白色上衣，长发",
		"status":"未受理",
		"rescueNum":5,
		"gmtCreate":1615462781
    }
  }
```



##### 返回参数说明

| 参数名      | 类型   | 说明                                                         |
| :---------- | :----- | ------------------------------------------------------------ |
| requestId   | string | 救援请求id                                                   |
| lostName    | string | 走失者姓名                                                   |
| lostAge     | number | 走失者年龄                                                   |
| lostGender  | string | 走失者性别                                                   |
| photo       | string | 走失者的图片url                                              |
| latitude    | number | 走失地点的纬度                                               |
| longitude   | number | 走失地点的经度                                               |
| lostAddress | string | 走失者的家庭住址                                             |
| lostPhone   | string | 家属的联系方式                                               |
| detail      | string | 补充信息                                                     |
| status      | string | 救援请求状态（0表示未受理，1表示进行中，2表示已完成，3表示已超时） |
| rescueNum   | number | 该案件接手的人数                                             |
| gmtCreate   | number | 案件的创建时间戳                                             |





## **7. 根据一定条件查看多条报案信息（救援请求）

##### 简要描述

- 根据url中的条件获取指定的报案信息列表，返回的列表默认以报案信息的发布时间降序排列。

##### 请求URL

- ` http://xx.com/task/ `

##### 请求方式

- GET

##### 参数

| 参数名 | 必选 | 类型   | 说明                                                         |
| :----- | :--- | :----- | ------------------------------------------------------------ |
| status | 否   | number | 期望得到指定状态的报案信息。空表示对返回信息的状态值没有限定，0表示未受理，1表示进行中，2表示已完成，3表示已超时 |
| uid    | 否   | string | 队员id。期望得到该队员接手的救援请求。空表示对队员没有限定。 |

##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": [
		{
			"requestId":"1",
			"lostName":"张三",
			"lostAge":70,
			"lostGender":"男",
			"photo":"https://xxxxx",
			"latitude":31.22,
			"longitude": 113.00,
			"lostAddress": "湖北省武汉市青山区翠园社区xxxx",
			"lostPhone": "13000000002",
			"detail":"走失时穿着白色上衣，长发",
			"status":"进行中",
			"rescueNum":5,
			"gmtCreate":1615462781
		},
		{
			"requestId":"2",
			"lostName":"李四",
			"lostAge":75,
			"lostGender":"男",
			"photo":"https://xxxxx",
			"latitude":31.22,
			"longitude": 113.00,
			"lostAddress": "湖北省武汉市青山区翠园社区xxxx",
			"lostPhone": "13000000002",
			"detail":"走失时穿着白色上衣，长发",
			"status":"进行中",
			"gmtCreate":1615462781
		}
    ]
  }
```

##### 返回参数说明

| 参数名      | 类型   | 说明                                                         |
| :---------- | :----- | ------------------------------------------------------------ |
| requestId   | string | 救援请求id                                                   |
| lostName    | string | 走失者姓名                                                   |
| lostAge     | number | 走失者年龄                                                   |
| lostGender  | string | 走失者性别                                                   |
| photo       | string | 走失者的图片url                                              |
| latitude    | number | 走失地点的纬度                                               |
| longitude   | number | 走失地点的经度                                               |
| lostAddress | string | 走失者的家庭住址                                             |
| lostPhone   | string | 家属的联系方式                                               |
| detail      | string | 补充信息                                                     |
| status      | string | 救援请求状态（0表示未受理，1表示进行中，2表示已完成，3表示已超时） |
| rescueNum   | number | 该案件接手的人数                                             |
| gmtCreate   | number | 案件的创建时间戳                                             |

##### 备注 

- 应用范围如下
- 队员使用小程序查看“我的救援请求”
- 后台管理系统按照状态展示所有的救援请求
- 后台管理系统绘制报案状态统计的饼状图
- 后台管理系统统计走失者年龄分布和区域分布

## 8. 队员查看系统中目前存在的救援请求

##### 简要描述

- 调用该接口可以展示系统中目前存在的救援请求。即只要是没有完成的救援请求，均以列表的方式返回（包括未受理、进行中、已超时状态）。队员在小程序查看可用的救援请求中，需要调用此接口。

##### 请求URL

- ` http://xx.com/task/available `

##### 请求方式

- GET


##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": [
		{
			"requestId":"1",
			"lostName":"张三",
			"lostAge":70,
			"lostGender":"男",
			"photo":"https://xxxxx",
			"latitude":31.22,
			"longitude": 113.00,
			"lostAddress": "湖北省武汉市青山区翠园社区xxxx",
			"lostPhone": "13000000002",
			"detail":"走失时穿着白色上衣，长发",
			"status":"未受理",
			"rescueNum":0,
			"gmtCreate":1615462781
		},
		{
			"requestId":"2",
			"lostName":"李四",
			"lostAge":75,
			"lostGender":"男",
			"photo":"https://xxxxx",
			"latitude":31.22,
			"longitude": 113.00,
			"lostAddress": "湖北省武汉市青山区翠园社区xxxx",
			"lostPhone": "13000000002",
			"detail":"走失时穿着白色上衣，长发",
			"status":"进行中",
			"rescueNum":5,
			"gmtCreate":1615462781
		}
    ]
  }
```

##### 返回参数说明

| 参数名      | 类型   | 说明                                                         |
| :---------- | :----- | ------------------------------------------------------------ |
| requestId   | string | 救援请求id                                                   |
| lostName    | string | 走失者姓名                                                   |
| lostAge     | number | 走失者年龄                                                   |
| lostGender  | string | 走失者性别                                                   |
| photo       | string | 走失者的图片url                                              |
| latitude    | number | 走失地点的纬度                                               |
| longitude   | number | 走失地点的经度                                               |
| lostAddress | string | 走失者的家庭住址                                             |
| lostPhone   | string | 家属的联系方式                                               |
| detail      | string | 补充信息                                                     |
| status      | string | 救援请求状态（0表示未受理，1表示进行中，2表示已完成，3表示已超时） |
| rescueNum   | number | 该案件接手的人数                                             |
| gmtCreate   | number | 案件的创建时间戳                                             |


## 9. 家属查看已发布的救援请求（暂时废弃）

##### 简要描述

- 调用该接口可以展示家属发布的救援请求。注意，为了简化，假定系统中只有一位家属。所以当家属查看发布的救援请求时，就等价于查看所有未受理/进行中/已超时的救援请求。调用该接口时，将返回这三种状态的救援请求列表。

##### 请求URL

- ` http://xx.com/task/family `

##### 请求方式

- GET

##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": [
		{
			"requestId":"1",
			"lostName":"张三",
			"lostAge":70,
			"lostGender":"男",
			"photo":"https://xxxxx",
			"latitude":31.22,
			"longitude": 113.00,
			"lostAddress": "湖北省武汉市青山区翠园社区xxxx",
			"lostPhone": "13000000002",
			"detail":"走失时穿着白色上衣，长发",
			"status":"未受理",
			"rescueNum":0,
			"gmtCreate":1615462781
		},
		{
			"requestId":"2",
			"lostName":"李四",
			"lostAge":75,
			"lostGender":男,
			"photo":"https://xxxxx",
			"latitude":31.22,
			"longitude": 113.00,
			"lostAddress": "湖北省武汉市青山区翠园社区xxxx",
			"lostPhone": "13000000002",
			"detail":"走失时穿着白色上衣，长发",
			"status":"进行中",
			"rescueNum":0,
			"gmtCreate":1615462781
		}
    ]
  }
```

##### 返回参数说明

| 参数名      | 类型   | 说明                                                         |
| :---------- | :----- | ------------------------------------------------------------ |
| requestId   | string | 救援请求id                                                   |
| lostName    | string | 走失者姓名                                                   |
| lostAge     | number | 走失者年龄                                                   |
| lostGender  | string | 走失者性别                                                   |
| photo       | string | 走失者的图片url                                              |
| latitude    | number | 走失地点的纬度                                               |
| longitude   | number | 走失地点的经度                                               |
| lostAddress | string | 走失者的家庭住址                                             |
| lostPhone   | string | 家属的联系方式                                               |
| detail      | string | 补充信息                                                     |
| status      | string | 救援请求状态（0表示未受理，1表示进行中，2表示已完成，3表示已超时） |
| rescueNum   | number | 该案件接手的人数                                             |
| gmtCreate   | number | 案件的创建时间戳                                             |


##### 备注

- 该接口原本功能为查看家属查看自己发布的救援请求。在这里直接简化为查看所有“未受理/进行中/已超时”状态的报案信息。后期在实际应用场景中该接口需要修改。

## 10. 队员受理一条报案信息

##### 简要描述

- 队员接受某条报案信息。

##### 请求URL

- ` http://xx.com/user/accept/ `

##### 请求方式

- POST

##### 参数

| 参数名    | 必选 | 类型   | 说明               |
| :-------- | :--- | :----- | ------------------ |
| uid       | 是   | string | 当前队员id         |
| requestId | 是   | string | 受理的救援请求的id |



##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": {
		"requestId":"1",
		"lostName":"张三",
		"lostAge":70,
		"lostGender":"男",
		"photo":"https://xxxxx",
		"latitude":31.22,
		"longitude": 113.00,
		"lostAddress": "湖北省武汉市青山区翠园社区xxxx",
		"lostPhone": "13000000002",
		"detail":"走失时穿着白色上衣，长发",
		"status":"进行中",
		"rescueNum":0,
		"gmtCreate":1615462781
	}
  }
```

##### 备注

- 返回该条报案信息


## 11. 队员完成一条报案信息的任务

##### 简要描述

- 队员完成某个救援请求。

##### 请求URL

- ` http://xx.com/user/complete/ `

##### 请求方式

- POST

##### 参数

| 参数名    | 必选 | 类型   | 说明               |
| :-------- | :--- | :----- | ------------------ |
| uid       | 是   | string | 当前队员id         |
| requestId | 是   | string | 完成的救援请求的id |



##### 返回示例 

``` 
   {
    "msg": "ok",
    "data": {
		"requestId":"1",
		"lostName":"张三",
		"lostAge":70,
		"lostGender":"男",
		"photo":"https://xxxxx",
		"latitude":31.22,
		"longitude": 113.00,
		"lostAddress": "湖北省武汉市青山区翠园社区xxxx",
		"lostPhone": "13000000002",
		"detail":"走失时穿着白色上衣，长发",
		"status":"已完成",
		"rescueNum":0,
		"gmtCreate":1615462781
	}
  }
```

##### 备注

- 受理失败后msg会指定失败原因。
- 返回该条报案信息


## 12. 获取和自己接手相同案件的队友

##### 简要描述

- 根据队员的id，获取和该队员接手相同案件的队友列表。案件的状态必须是进行中。

##### 请求URL

- ` http://xx.com/user/partner/{uid} `

##### 请求方式

- GET

##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": [
		{
			"uid": "1",
			"username": "张三",
			"phone": "13000000001",
			"age": 20,
			"gender": "男" ,
			"address": "武汉市洪山区珞喻路129号武汉大学信息学部",
			"longitude":113.00,
			"latitude":32.12,
			"gmtCreate":1615462781,
			"gmtModified":1615462952,
			"cases":[
				{
					"requestId":"2",
					"lostName":"李四",
					"lostAge":75,
					"lostGender":"男",
					"photo":"https://xxxxx",
					"latitude":31.22,
					"longitude": 113.00,
					"lostAddress": "湖北省武汉市青山区翠园社区xxxx",
					"lostPhone": "13000000002",
					"detail":"走失时穿着白色上衣，长发",
					"status":"进行中",
					"rescueNum":0,
					"gmtCreate":1615462781
				}
			]
		}
	]
  }
```

##### 返回参数说明 

| 参数名      | 类型   | 说明                             |
| :---------- | :----- | -------------------------------- |
| uid         | string | 队员id                           |
| username    | string | 队员姓名                         |
| phone       | string | 队员电话号码                     |
| age         | number | 队员年龄                         |
| gender      | string | 队员性别                         |
| address     | string | 队员家庭住址                     |
| longitude   | number | 队员所在位置的经度               |
| latitude    | number | 队员所在位置的纬度               |
| gmtCreate   | number | 队员的创建时间                   |
| gmtModified | number | 队员的修改时间                   |
| cases       | list   | 队员接手的，正在进行中的案件集合 |

其中cases中的对象的描述如下表

| 参数名      | 类型   | 说明                                                         |
| :---------- | :----- | ------------------------------------------------------------ |
| requestId   | string | 救援请求id                                                   |
| lostName    | string | 走失者姓名                                                   |
| lostAge     | number | 走失者年龄                                                   |
| lostGender  | string | 走失者性别                                                   |
| photo       | string | 走失者的图片url                                              |
| latitude    | number | 走失地点的纬度                                               |
| longitude   | number | 走失地点的经度                                               |
| lostAddress | string | 走失者的家庭住址                                             |
| lostPhone   | string | 家属的联系方式                                               |
| detail      | string | 补充信息                                                     |
| status      | string | 救援请求状态（0表示未受理，1表示进行中，2表示已完成，3表示已超时） |
| rescueNum   | number | 该案件接手的人数                                             |
| gmtCreate   | number | 案件的创建时间戳                                             |

##### 备注

- 返回数据的data是一个存放队员对象的列表，而队员对象中的cases属性也是一个列表，它存放了该队员接手的进行中的案件


## **13. 管理员添加新队员

##### 简要描述

- 添加新成员

##### 请求URL

- ` http://xx.com/user/admin/member `

##### 请求方式

- POST

##### 参数

| 参数名   | 必选 | 类型   | 说明                    |
| :------- | :--- | :----- | ----------------------- |
| username | 是   | string | 队员姓名                |
| phone    | 是   | string | 队员电话号码            |
| wxId     | 是   | string | 队员微信号              |
| birth    | 是   | string | 队员出生年月 yyyy-MM-dd |
| gender   | 是   | string | 队员性别                |
| address  | 是   | string | 队员家庭住址            |


##### 返回示例 

``` 
  {
    "msg": "ok",
    "data":{}
  }
```


## **14. 管理员删除队员

##### 简要描述

- 删除指定id的队员

##### 请求URL

- ` http://xx.com/user/admin/member/{uid} `

##### 请求方式

- DELETE


##### 返回示例 

``` 
  {
    "msg": "ok",
    "data":{}
  }
```



## **15. 管理员编辑队员信息

##### 简要描述

- 编辑指定id的队员信息

##### 请求URL

- ` http://xx.com/user/member/{uid} `

##### 请求方式

- POST

##### 参数

| 参数名   | 必选 | 类型   | 说明                    |
| :------- | :--- | :----- | ----------------------- |
| username | 否   | string | 队员姓名                |
| phone    | 否   | string | 队员电话号码            |
| wxId     | 否   | string | 队员微信号              |
| birth    | 否   | string | 队员出生年月 yyyy-MM-dd |
| gender   | 否   | string | 队员性别                |
| address  | 否   | string | 队员家庭住址            |


##### 返回示例 

``` 
  {
    "msg": "ok",
    "data":{}
  }
```


## **16. 管理员获取所有队员的信息

##### 简要描述

- 返回所有队员信息的列表。其中队员对象的cases属性包含该队员接手的进行中的案件。

##### 请求URL

- ` http://xx.com/user/admin/member `

##### 请求方式

- GET

##### 返回示例 

``` 
  {
    "msg": "ok",
    "data": [
		{
			"uid": "1",
			"username": "张三",
			"phone": "13000000001",
			"age": 20,
			"gender": "男" ,
			"address": "武汉市洪山区珞喻路129号武汉大学信息学部",
			"longitude":113.00,
			"latitude":32.12,
			"gmtCreate":1615462781,
			"gmtModified":1615462952,
			"cases":[
				{
					"requestId":"2",
					"lostName":"李四",
					"lostAge":75,
					"lostGender":"男",
					"photo":"https://xxxxx",
					"latitude":31.22,
					"longitude": 113.00,
					"lostAddress": "湖北省武汉市青山区翠园社区xxxx",
					"lostPhone": "13000000002",
					"detail":"走失时穿着白色上衣，长发",
					"status":"进行中",
					"rescueNum":0,
					"gmtCreate":1615462781
				}
			]
		}
	]
  }
```

##### 返回参数说明 

| 参数名      | 类型   | 说明                             |
| :---------- | :----- | -------------------------------- |
| uid         | string | 队员id                           |
| username    | string | 队员姓名                         |
| phone       | string | 队员电话号码                     |
| age         | number | 队员年龄                         |
| gender      | string | 队员性别                         |
| address     | string | 队员家庭住址                     |
| longitude   | number | 队员所在位置的经度               |
| latitude    | number | 队员所在位置的纬度               |
| gmtCreate   | number | 队员的创建时间                   |
| gmtModified | number | 队员的修改时间                   |
| cases       | list   | 队员接手的，正在进行中的案件集合 |

其中cases中的对象的描述如下表

| 参数名      | 类型   | 说明                                                         |
| :---------- | :----- | ------------------------------------------------------------ |
| requestId   | string | 救援请求id                                                   |
| lostName    | string | 走失者姓名                                                   |
| lostAge     | number | 走失者年龄                                                   |
| lostGender  | string | 走失者性别                                                   |
| photo       | string | 走失者的图片url                                              |
| latitude    | number | 走失地点的纬度                                               |
| longitude   | number | 走失地点的经度                                               |
| lostAddress | string | 走失者的家庭住址                                             |
| lostPhone   | string | 家属的联系方式                                               |
| detail      | string | 补充信息                                                     |
| status      | string | 救援请求状态（0表示未受理，1表示进行中，2表示已完成，3表示已超时） |
| rescueNum   | number | 该案件接手的人数                                             |
| gmtCreate   | number | 案件的创建时间戳                                             |

## 17. 上传图片文件的接口

##### 简要描述

- 家属发布信息时上传老人图片

##### 请求URL

- ` http://xx.com/image/upload `

##### 请求方式

- POST

##### 返回示例 

``` 
  {
    "msg": "ok",
    "url":"http://xxxxx"
  }
```

| 参数名 | 类型   | 说明                  |
| :----- | :----- | --------------------- |
| msg    | string | 提示消息              |
| url    | string | 上传图片后，图片的url |

## 18. 将队员拍的照片和系统中的照片进行比对

##### 简要描述

- 根据某张照片的url，将队员拍的照片和系统中的照片进行比对，返回最相似的照片url。如果相似度低于某阈值，将出现警告信息。

##### 请求URL

- ` http://xx.com/image/match `

##### 请求方式

- GET

##### 参数

| 参数名 | 必选 | 类型   | 说明                |
| :----- | :--- | :----- | ------------------- |
| srcUrl | 是   | string | 进行比对的图片的url |

##### 返回示例 

``` 
  {
    "msg": "ok",
    "url":"http://xxxxx"
  }
```

| 参数名 | 类型   | 说明                                  |
| :----- | :----- | ------------------------------------- |
| msg    | string | 提示消息，ok或fail                    |
| url    | string | 比对得到的最相似的图片的url，可以为空 |
