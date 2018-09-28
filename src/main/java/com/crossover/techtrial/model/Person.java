/**
 * 
 */
package com.crossover.techtrial.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


/**
 * @author crossover
 *
 */
@Entity
@Table(name = "person")
public class Person implements Serializable{

  private static final long serialVersionUID = 7401548380514451401L;
  
  public Person() {}
  

  public Person(Long i) {id = i;}
 
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "name")
  String name;
  

  @NotNull
  @Email
  @Column(name = "email")
  String email;

  @Column(name = "regisNum")
  String regisNum;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  

public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  
  public String getRegisNum() {
	return regisNum;
}

public void setRegisNum(String regisNum) {
	this.regisNum = regisNum;
}

@Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((regisNum == null) ? 0 : regisNum.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Person other = (Person) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (regisNum == null) {
      if (other.regisNum != null)
        return false;
    } else if (!regisNum.equals(other.regisNum))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Person [id=" + id + ", name=" + name + ", email=" + email + ", registrationNumber=" + regisNum + "]";
  }
  
  


}
