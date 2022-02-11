package main

import (
	"encoding/json"
	"fmt"
	"github.com/gin-gonic/gin"
	"io/ioutil"
	"log"
	"net/http"
)

type Status struct {
	Status string `json:"status"`
}
type User struct {
	Id   string `json:"id"`
	Name string `json:"name"`
}

func main() {

	sidecarUrl := "http://localhost:1014"

	gin.SetMode(gin.ReleaseMode)

	r := gin.Default()

	r.GET("/health.json", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"status": "UP",
		})
	})

	r.GET("/api/sc/b", func(c *gin.Context) {

		msg := "go-web UP call ServiceMesh gateway --> "

		//调用 ServiceMesh 网关
		resp, err := http.Get("http://172.16.20.185:30061/api/sc/b")
		if err != nil {
			msg = fmt.Sprintf("%v StatusCode:[%v] msg:[%v]", msg, http.StatusInternalServerError, "ERROR:"+err.Error())
		} else {
			defer resp.Body.Close()
			body, _ := ioutil.ReadAll(resp.Body)
			msg = fmt.Sprintf("%v StatusCode:[%v] msg:[%v]", msg, http.StatusOK, string(body))
		}
		log.Println(msg)

		c.JSON(http.StatusOK, gin.H{"msg": msg})
	})
	r.GET("/test-env", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{"name": "go-test"})
	})

	///env/type

	//通过spring-cloud-netflix-sidecar集成的zuul proxy远程调用微服务
	r.GET("/test/java", func(c *gin.Context) {

		resp, err := http.Get(sidecarUrl + "/gs-consumer/test/user")

		if err != nil || resp.StatusCode != http.StatusOK {
			log.Println(err.Error())
			c.Status(http.StatusServiceUnavailable)
			return
		}

		var users []User

		defer resp.Body.Close()

		contents, err := ioutil.ReadAll(resp.Body)

		json.Unmarshal(contents, &users)

		c.JSON(http.StatusOK, users)
	})

	log.Print("sidecar-url ---> ", sidecarUrl)

	r.Run(":3000")
}