<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>${username}</h5>
    ${message?ifExists}
    <form method="post">


        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> E-mail:</label>
            <div class="col-sm-10">
                <input type="email" name="email" placeholder="some@some.com" value="${email!''}"/>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Password:</label>
            <div class="col-sm-10">
                <input type="password" name="password" placeholder="Password"/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        <button type="submit" class="btn btn-primary">Save</button>
    </form>
</@c.page>